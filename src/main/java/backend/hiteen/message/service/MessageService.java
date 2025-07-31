package backend.hiteen.message.service;


import backend.hiteen.board.entity.Board;
import backend.hiteen.board.exception.BoardNotFoundException;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.global.exception.BusinessException;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.MemberNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import backend.hiteen.message.dto.request.MessageRequest;
import backend.hiteen.message.dto.response.MessageResponse;
import backend.hiteen.message.dto.response.MessageRoomListResponse;
import backend.hiteen.message.entity.Message;
import backend.hiteen.message.entity.MessageRoom;
import backend.hiteen.message.exception.*;
import backend.hiteen.message.mapper.MessageMapper;
import backend.hiteen.message.repository.MessageRepository;
import backend.hiteen.message.repository.MessageRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MessageAsyncService messageAsyncService;


    public MessageResponse toDto(Message message, Long memberId) {
        return MessageMapper.toDto(message, memberId);
    }

    // 대화 방 생성 및 메시지 전송
    @Transactional
    public Message sendMessage(MessageRequest request, Long memberId) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(BoardNotFoundException::new);

        Long receiverId;
        final Integer anonNum;

        // 작성자에게 보내는 경우
        if (Boolean.TRUE.equals(request.getIsBoardWriter())) {
            receiverId = board.getMember().getId();
            anonNum = null;
            if (receiverId.equals(memberId)) throw new CannotSendMessageToSelfException();
        }
        // 익명 댓글러에게 보내는 경우
        else if (request.getAnonymousNumber() != null) {
            Comment comment = commentRepository
                    .findByBoardIdAndAnonymousNumber(board.getId(), request.getAnonymousNumber())
                    .orElseThrow(CommentAnonymousNotFoundException::new);
            receiverId = comment.getMember().getId();
            anonNum = comment.getAnonymousNumber();
            if (receiverId.equals(memberId)) throw new CannotSendMessageToSelfException();
        }
        else {
            throw new MessageTargetNotSpecifiedException();
        }

        Member sender = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(MemberNotFoundException::new);

        if (!sender.getSchool().getId().equals(receiver.getSchool().getId())) {
            throw new BusinessException(ErrorCode.MESSAGE_PERMISSION_DENIED);
        }

        MessageRoom room = messageRoomRepository
                .findByBoardAndParticipantsAndAnon(
                        board.getId(), memberId, receiverId, anonNum
                )
                .orElseGet(() -> messageRoomRepository.save(
                        MessageRoom.builder()
                                .board(board)
                                .senderId(memberId)
                                .receiverId(receiverId)
                                .anonymousNumber(anonNum)
                                .build()
                ));

        Message message = Message.builder()
                .messageRoom(room)
                .senderId(memberId)
                .receiverId(receiverId)
                .content(request.getContent())
                .build();
        return messageRepository.save(message);
    }


    // 대화 방 내 메세지 전송

    @Transactional
    public Message sendMessageInRoom(Long roomId, Long memberId, String content) {
        MessageRoom room = messageRoomRepository.findById(roomId)
                .orElseThrow(MessageRoomNotFoundException::new);

        if (!memberId.equals(room.getSenderId()) && !memberId.equals(room.getReceiverId())) {
            throw new MessageRoomNotOwnerException();
        }

        Long otherId = room.getSenderId().equals(memberId)
                ? room.getReceiverId()
                : room.getSenderId();

        Message message = Message.builder()
                .messageRoom(room)
                .senderId(memberId)
                .receiverId(otherId)
                .content(content)
                .build();

        return messageRepository.save(message);
    }


    //특정 방 대화 전체 조회
    @Transactional
    public List<MessageResponse> getMessages(Long roomId, Long memberId) {
        readAllUnreadMessages(roomId, memberId);

        List<Message> messages = messageRepository.findByMessageRoomIdOrderByCreatedAtAsc(roomId);
        return messages.stream()
                .map(m -> toDto(m, memberId))
                .toList();
    }

    public List<MessageRoomListResponse> getMyMessageRooms(Long memberId) {
        List<MessageRoom> rooms = messageRoomRepository.findAllByMemberOrderByUpdatedAtDesc(memberId);

        List<MessageRoomListResponse> result = new ArrayList<>();
        for (MessageRoom room : rooms) {
            Message lastMsg = messageRepository
                    .findTopByMessageRoomIdOrderByCreatedAtDesc(room.getId())
                    .orElse(null);

            String lastMessage = lastMsg != null ? lastMsg.getContent() : null;
            LocalDateTime lastMessageTime = lastMsg != null ? lastMsg.getCreatedAt() : null;
            String lastMessageNickname = lastMsg != null ? MessageMapper.computeDisplayName(lastMsg) : null;

            Long targetId = room.getSenderId().equals(memberId) ? room.getReceiverId() : room.getSenderId();
            String targetNickname = MessageMapper.computeDisplayName(room, targetId);

            int unreadCount = messageRepository.countByMessageRoomIdAndReceiverIdAndIsReadFalse(room.getId(), memberId);

            result.add(new MessageRoomListResponse(
                    room.getId(),
                    room.getBoard().getId(),
                    room.getBoard().getTitle(),
                    lastMessage,
                    lastMessageTime,
                    lastMessageNickname,
                    unreadCount,
                    targetNickname
            ));
        }
        return result;
    }

    //롱폴링
    public DeferredResult<ResponseEntity<ApiResponse<List<MessageResponse>>>> pollMessages(Long roomId, Long lastMessageId, Long memberId) {
        long timeout = 30_000L;
        DeferredResult<ResponseEntity<ApiResponse<List<MessageResponse>>>> result =
                new DeferredResult<>(timeout);

        messageAsyncService.pollMessagesAsync(result, roomId, lastMessageId, memberId);

        result.onTimeout(() -> result.setResult(
                ResponseEntity.ok(ApiResponse.success(SuccessCode.MESSAGE_POLLED, List.of()))
        ));

        return result;
    }

    public void readAllUnreadMessages(Long roomId, Long memberId) {
        messageRepository.markAllAsRead(roomId, memberId);
    }
}
package backend.hiteen.message.service;


import backend.hiteen.board.entity.Board;
import backend.hiteen.board.exception.BoardNotFoundException;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.message.dto.request.MessageRequest;
import backend.hiteen.message.dto.response.MessageResponse;
import backend.hiteen.message.entity.Message;
import backend.hiteen.message.entity.MessageRoom;
import backend.hiteen.message.exception.*;
import backend.hiteen.message.repository.MessageRepository;
import backend.hiteen.message.repository.MessageRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MessageRoomRepository messageRoomRepository;


    public MessageResponse toDto(Message message, Long memberId) {
        return new MessageResponse(
                message.getId(),
                message.getMessageRoom().getId(),
                message.getContent(),
                message.getCreatedAt(),
                computeDisplayName(message),
                message.getSenderId().equals(memberId)
        );
    }

    // 대화 방 생성 및 메시지 전송
    @Transactional
    public Message sendMessage(MessageRequest request, Long memberId) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(BoardNotFoundException::new);

        Long receiverId;

        //둘 다 동시에 값 들어오지 않게
        if (Boolean.TRUE.equals(request.getIsBoardWriter()) && request.getAnonymousNumber() != null) {
            throw new InvalidMessageTargetException();
        }

        //작성자에게 쪽지보냄
        if (Boolean.TRUE.equals(request.getIsBoardWriter())) {
            receiverId = board.getMember().getId();

            if (receiverId.equals(memberId)) {
                throw new CannotSendMessageToSelfException();
            }
            //익명 댓글러에게 쪽지보냄
        } else if (request.getAnonymousNumber() != null) {
            Comment comment = commentRepository.findByBoardIdAndAnonymousNumber(
                    request.getBoardId(), request.getAnonymousNumber()
            ).orElseThrow(CommentAnonymousNotFoundException::new);

            receiverId = comment.getMember().getId();

            if (receiverId.equals(memberId)) {
                throw new CannotSendMessageToSelfException();
            }

        } else {
            throw new MessageTargetNotSpecifiedException();
        }

        Optional<MessageRoom> optionalRoom =
                messageRoomRepository.findByBoardIdAndParticipants(
                        request.getBoardId(), memberId, receiverId
                );

        MessageRoom messageRoom = optionalRoom.orElse(null);

        if (messageRoom == null) {
            messageRoom = MessageRoom.builder()
                    .board(board)
                    .senderId(memberId)
                    .receiverId(receiverId)
                    .build();

            messageRoom = messageRoomRepository.save(messageRoom);
        }

        Message message = Message.builder()
                .messageRoom(messageRoom)
                .senderId(memberId)
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
            throw new IllegalArgumentException("이 쪽지방에 메시지를 보낼 권한이 없습니다.");
        }
        Message message = Message.builder()
                .messageRoom(room)
                .senderId(memberId)
                .content(content)
                .build();

        return messageRepository.save(message);
    }


    //특정 방 대화 전체 조회
    public List<Message> getMessages(Long roomId) {
        return messageRepository.findByMessageRoomIdOrderByCreatedAtAsc(roomId);
    }

    public List<Message> getMessageAfter(Long roomId, Long lastMessageId) {
        return messageRepository.findByMessageRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .filter(m -> m.getId() > lastMessageId)
                .toList();
    }

    //롱폴링
    public DeferredResult<ResponseEntity<ApiResponse<List<MessageResponse>>>> pollMessages(Long roomId, Long lastMessageId, Long memberId) {
        long timeout = 30_000L;
        DeferredResult<ResponseEntity<ApiResponse<List<MessageResponse>>>> result =
                new DeferredResult<>(timeout);

        new Thread(() -> {
            List<MessageResponse> body = getMessageAfter(roomId, lastMessageId).stream()
                    .map(m -> toDto(m, memberId))
                    .toList();
            ResponseEntity<ApiResponse<List<MessageResponse>>> response =
                    ResponseEntity
                            .status(SuccessCode.MESSAGE_POLLED.getStatus())
                            .body(ApiResponse.success(SuccessCode.MESSAGE_POLLED, body));

            result.setResult(response);
        }).start();

        return result;
    }


    // 작성자라면 작성자 댓글이면 익명번호 기반으로 조회
    public String computeDisplayName(Message message) {
        Board board = message.getMessageRoom().getBoard();
        Long boardOwnerId = board.getMember().getId();

        // 메시지 보낸 사람이 게시글 작성자라면
        if (message.getSenderId().equals(boardOwnerId)) {
            return "작성자";
        } else {
            // 댓글을 통해 부여된 익명 번호 조회
            Optional<Comment> commentOptional = commentRepository.findByBoardIdAndMemberId(board.getId(), message.getSenderId());
            return commentOptional.map(comment -> "익명 " + comment.getAnonymousNumber())
                    .orElse("익명");
        }
    }

}
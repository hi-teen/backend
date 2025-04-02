package backend.hiteen.message.service;


import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.message.controller.MessageController;
import backend.hiteen.message.entity.Message;
import backend.hiteen.message.entity.MessageRoom;
import backend.hiteen.message.repository.MessageRepository;
import backend.hiteen.message.repository.MessageRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MessageRoomRepository messageRoomRepository;


    // 대화 방 생성 및 메시지 전송
    @Transactional
    public Message sendMessage(Long boardId, Long senderId, Long receiverId, String content) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        Optional<MessageRoom> optionalRoom = messageRoomRepository.findMessageRoomByBoardIdAndSenderIdAndReceiverId(boardId, senderId, receiverId);
        MessageRoom messageRoom = optionalRoom.orElse(null);


        if (messageRoom == null) {
            messageRoom = MessageRoom.builder()
                    .board(board)
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .createdAt(LocalDateTime.now())
                    .build();
            messageRoom = messageRoomRepository.save(messageRoom);
        }

        Message message = Message.builder()
                .messageRoom(messageRoom)
                .senderId(senderId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    // 대화 방 내 메세지 전송

    @Transactional
    public Message sendMessageInRoom(Long roomId, Long senderId, String content) {
        MessageRoom room = messageRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("대화방이 존재하지 않습니다."));
        Message message = Message.builder()
                .messageRoom(room)
                .senderId(senderId)
                .content(content)
                .createdAt(LocalDateTime.now())
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
    public DeferredResult<ResponseEntity<List<MessageController.MessageResponse>>> pollMessages(Long roomId, Long lastMessageId) {
        long timeout = 30000L;
        DeferredResult<ResponseEntity<List<MessageController.MessageResponse>>> deferredResult = new DeferredResult<>(timeout);

        new Thread(() -> {
            long elapsed = 0;
            while (elapsed < timeout) {
                List<Message> newMessages = getMessageAfter(roomId, lastMessageId);

                if (!newMessages.isEmpty()) {
                    List<MessageController.MessageResponse> responses = newMessages.stream()
                            .map(m ->{
                                String nickname = computeDisplayName(m);
                                return new MessageController.MessageResponse(
                                        m.getId(),
                                        m.getMessageRoom().getId(),
                                        m.getSenderId(),
                                        m.getContent(),
                                        m.getCreatedAt(),
                                        nickname
                                );
                            })
                            .toList();
                    deferredResult.setResult(ResponseEntity.ok(responses));
                    return;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                    return;
                }
                elapsed += 1000;
            }
            deferredResult.setResult(ResponseEntity.ok(Collections.emptyList()));
        }).start();
        return deferredResult;
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

package backend.hiteen.message.service;

import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.message.dto.response.MessageResponse;
import backend.hiteen.message.entity.Message;
import backend.hiteen.message.entity.MessageRoom;
import backend.hiteen.message.mapper.MessageMapper;
import backend.hiteen.message.repository.MessageRepository;
import backend.hiteen.message.repository.MessageRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageAsyncService {

    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final CommentRepository commentRepository;


    @Async
    public void pollMessagesAsync(
            DeferredResult<ResponseEntity<ApiResponse<List<MessageResponse>>>> result,
            Long roomId, Long lastMessageId, Long memberId) {
        try {
            MessageRoom room = messageRoomRepository.findById(roomId)
                    .orElse(null);
            if (room == null || (!room.getSenderId().equals(memberId) && !room.getReceiverId().equals(memberId))) {
                result.setErrorResult(
                        ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ApiResponse.failure(ErrorCode.MESSAGE_ROOM_NOT_FOUND))
                );
                return;
            }

            // 2. 새 메시지 조회
            List<Message> newMessages = messageRepository.findByMessageRoomIdOrderByCreatedAtAsc(roomId)
                    .stream()
                    .filter(m -> m.getId() > lastMessageId)
                    .toList();

            List<MessageResponse> body = newMessages.stream()
                    .map(m -> MessageMapper.toDto(m, memberId, commentRepository))
                    .toList();

            ResponseEntity<ApiResponse<List<MessageResponse>>> response =
                    ResponseEntity.status(SuccessCode.MESSAGE_POLLED.getStatus())
                            .body(ApiResponse.success(SuccessCode.MESSAGE_POLLED, body));
            result.setResult(response);
        } catch (Exception e) {
            result.setErrorResult(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR))
            );
        }
    }
}
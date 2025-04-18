package backend.hiteen.message.controller;

import backend.hiteen.message.entity.Message;
import backend.hiteen.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("messages")
@RequiredArgsConstructor
@Tag(name = "message", description = "쪽지 API")
//TODO: @Operation(summary, description) 추가 + 쪽지방 생성 조건
//TODO: 모든 필드에 @Schema(description, example) 작성
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    @Operation(summary = "쪽지 보내기", description = "쪽지를 보냅니다.")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        Message message = messageService.sendMessage(
                request.getBoardId(),
                request.getSenderId(),
                request.getReceiverId(),
                request.getContent()
        );

        MessageResponse response = convertToResponse(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/room/{roomId}/send")
    @Operation(summary = "쪽지방 내 메세지 전송", description = "기존 쪽지방에 쪽지를 보냅니다.")
    public ResponseEntity<MessageResponse> sendMessageInRoom(@PathVariable Long roomId,
                                                             @RequestBody RoomMessageRequest request) {
        Message message = messageService.sendMessageInRoom(
                roomId,
                request.getSenderId(),
                request.getContent()
        );

        MessageResponse response = convertToResponse(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "쪽지방 메시지 조회", description = "특정 쪽지방의 전체 메시지를 조회합니다.")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long roomId) {
        List<Message> messages = messageService.getMessages(roomId);
        List<MessageResponse> responseList = messages.stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/room/{roomId}/poll")
    @Operation(summary = "롱폴링 메시지 수신", description = "새 메시지가 올 때까지 대기하여 전달합니다.")
    public DeferredResult<ResponseEntity<List<MessageResponse>>> pollMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") Long lastMessageId) {
        return messageService.pollMessages(roomId, lastMessageId);
    }

    private MessageResponse convertToResponse(Message message) {
        String chatNickname = messageService.computeDisplayName(message);
        return new MessageResponse(
                message.getId(),
                message.getMessageRoom().getId(),
                message.getSenderId(),
                message.getContent(),
                message.getCreatedAt(),
                chatNickname
        );
    }


    // 요청 dto (처음 메시지)
    @Data
    public static class MessageRequest {
        private Long boardId;
        private Long senderId;
        private Long receiverId;
        private String content;
    }

    // 요청 DTO (대화방 내 메시지)
    @Data
    public static class RoomMessageRequest {
        private Long senderId;
        private String content;
    }

    //응답 dto
    @Data
    public static class MessageResponse {
        private Long messageId;
        private Long roomId;
        private Long senderId;
        private String content;
        private LocalDateTime createdAt;
        private String chatNickname;

        public MessageResponse(Long messageId, Long roomId, Long senderId, String content, LocalDateTime createdAt, String chatNickname) {
            this.messageId = messageId;
            this.roomId = roomId;
            this.senderId = senderId;
            this.content = content;
            this.createdAt = createdAt;
            this.chatNickname = chatNickname;
        }

    }

}

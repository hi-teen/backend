package backend.hiteen.message.controller;

import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.message.dto.request.MessageRequest;
import backend.hiteen.message.dto.request.MessageRoomRequest;
import backend.hiteen.message.dto.response.MessageResponse;
import backend.hiteen.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Message", description = "쪽지 API")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    @Operation(summary = "쪽지 보내기", description = "새 쪽지를 보냅니다.")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @RequestBody MessageRequest request) {
        MessageResponse response = messageService.toDto(
                messageService.sendMessage(
                        request.getBoardId(),
                        request.getSenderId(),
                        request.getReceiverId(),
                        request.getContent()
                )
        );
        return ResponseEntity
                .status(SuccessCode.MESSAGE_SENT.getStatus())
                .body(ApiResponse.success(SuccessCode.MESSAGE_SENT, response));
    }

    @PostMapping("/room/{roomId}/send")
    @Operation(summary = "쪽지방 내 메시지 전송", description = "기존 쪽지방에 쪽지를 전송합니다.")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessageInRoom(
            @PathVariable Long roomId,
            @RequestBody MessageRoomRequest request) {
        MessageResponse response = messageService.toDto(
                messageService.sendMessageInRoom(
                        roomId,
                        request.getSenderId(),
                        request.getContent()
                )
        );
        return ResponseEntity
                .status(SuccessCode.MESSAGE_SENT_IN_ROOM.getStatus())
                .body(ApiResponse.success(SuccessCode.MESSAGE_SENT_IN_ROOM, response));
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "쪽지방 메시지 조회", description = "특정 쪽지방의 전체 메시지를 조회합니다.")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(
            @PathVariable Long roomId) {
        List<MessageResponse> responseList = messageService.getMessages(roomId).stream()
                .map(messageService::toDto)
                .toList();
        return ResponseEntity
                .ok(ApiResponse.success(SuccessCode.MESSAGES_FETCHED, responseList));
    }

    @GetMapping("/room/{roomId}/poll")
    @Operation(summary = "롱폴링 메시지 수신", description = "새 메시지가 올 때까지 대기하여 전달합니다.")
    public DeferredResult<ResponseEntity<ApiResponse<List<MessageResponse>>>> pollMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") Long lastMessageId) {
        return messageService.pollMessages(roomId, lastMessageId);
    }
}

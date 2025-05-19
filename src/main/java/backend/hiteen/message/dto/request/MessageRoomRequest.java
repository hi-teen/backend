package backend.hiteen.message.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MessageRoomRequest {
    @Schema(description = "발신자 ID", example = "2")
    private Long senderId;

    @Schema(description = "쪽지 내용", example = "안녕하세요?")
    private String content;
}

package backend.hiteen.message.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull
    @Schema(description = "게시글 ID", example = "1")
    private Long boardId;

    @NotNull
    @Schema(description = "발신자 ID", example = "2")
    private Long senderId;

    @NotNull
    @Schema(description = "수신자 ID", example = "3")
    private Long receiverId;

    @NotNull
    @Schema(description = "쪽지 내용", example = "안녕하세요!")
    private String content;
}

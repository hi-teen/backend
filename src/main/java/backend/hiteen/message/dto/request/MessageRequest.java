package backend.hiteen.message.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull
    @Schema(description = "게시글 ID", example = "1")
    private Long boardId;

    @Schema(description = "받는 사람이 게시글 작성자인 경우 true", example = "true")
    private Boolean isBoardWriter;

    @Schema(description = "익명 번호(댓글러에게 쪽지 보낼 때)", example = "3")
    private Integer anonymousNumber;

    @NotNull
    @Schema(description = "쪽지 내용", example = "안녕하세요!")
    private String content;
}

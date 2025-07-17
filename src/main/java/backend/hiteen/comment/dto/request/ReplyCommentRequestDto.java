package backend.hiteen.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyCommentRequestDto {
    @Schema(description = "대댓글 내용", example = "저도요!")
    private String content;
}

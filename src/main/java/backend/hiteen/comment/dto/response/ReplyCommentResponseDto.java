package backend.hiteen.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// TODO: 모든 필드에 @Schema(description, example) 작성 필요
public class ReplyCommentResponseDto {

    @Schema(description = "대댓글 id", example = "2")
    private Long replyId;

    @Schema(description = "대댓글 내용", example = "저도요!")
    private String content;

    @Schema(description = "익명 번호")
    private int anonymousNumber;
}

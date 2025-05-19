package backend.hiteen.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @Schema(description = "작성 게시글 id", example = "1")
    private Long boardId;

    @Schema(description = "해당 게시글에 작성할 댓글의 내용", example = "낼 학교 기대 돼!")
    private String content;
}

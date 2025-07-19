package backend.hiteen.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponseDto {

    @Schema(description = "댓글 id")
    private Long commentId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "익명 번호")
    private int anonymousNumber;

    private LocalDateTime createdAt;

    private int likeCount;
    private boolean likedByMe;

    //대댓글
    @Schema(description = "해당 댓글에 달린 대댓글 목록")
    private List<ReplyCommentResponseDto> replies;

}

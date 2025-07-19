package backend.hiteen.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReplyCommentResponseDto {

    @Schema(description = "대댓글 id")
    private Long replyId;

    @Schema(description = "대댓글 내용")
    private String content;

    @Schema(description = "익명 번호", example = "3")
    private int anonymousNumber;

    private LocalDateTime createdAt;

    private int likeCount;
    private boolean likedByMe;

}

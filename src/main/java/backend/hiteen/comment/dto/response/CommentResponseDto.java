package backend.hiteen.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private int anonymousNumber;

    //대댓글
    private List<ReplyCommentResponseDto> replies;
}

package backend.hiteen.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyCommentResponseDto {
    private Long replyId;
    private String content;
    private int anonymousNumber;
}

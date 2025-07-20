package backend.hiteen.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikeResponse {
    private int likeCount;
    private boolean liked;
}

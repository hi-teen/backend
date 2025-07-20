package backend.hiteen.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyCommentResponse {
    private Long boardId;
    private String boardTitle;
    private Long commentId;
    private String content;
    private boolean isReply;
    private LocalDateTime createdAt;
}

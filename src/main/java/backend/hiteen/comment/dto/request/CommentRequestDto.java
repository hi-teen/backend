package backend.hiteen.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String content;
    private Long boardId;
}

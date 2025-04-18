package backend.hiteen.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// TODO: 모든 필드에 @Schema(description, example) 작성 필요
public class ReplyCommentResponseDto {
    private Long replyId;
    private String content;
    private int anonymousNumber;
}

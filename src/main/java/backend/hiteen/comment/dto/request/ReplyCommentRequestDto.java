package backend.hiteen.comment.dto.request;

import lombok.Getter;

@Getter
// TODO: 모든 필드에 @Schema(description, example) 작성 필요
public class ReplyCommentRequestDto {
    private String content;
}

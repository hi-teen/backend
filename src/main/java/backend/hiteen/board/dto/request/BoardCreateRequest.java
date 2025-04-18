package backend.hiteen.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//TODO: @Schema(description = "오늘 급식 뭐냐", example = "배고프다", required = true) 이런식으로 각 필드별로 스키마 작성.
public class BoardCreateRequest {

    private Long memberId;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}





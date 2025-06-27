package backend.hiteen.board.dto.request;

import backend.hiteen.board.entity.DisclosureStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//TODO: @Schema(description = "오늘 급식 뭐냐", example = "배고프다", required = true) 이런식으로 각 필드별로 스키마 작성.
public class BoardCreateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "실명 공개 여부를 선택해주세요.")
    @Schema(
            description = "작성자 실명 공개 여부\n- PUBLIC: 실명 공개\n- ANONYMOUS: 익명",
            example = "ANONYMOUS",
            allowableValues = {"PUBLIC", "ANONYMOUS"}
    )
    private DisclosureStatus disclosureStatus;

}





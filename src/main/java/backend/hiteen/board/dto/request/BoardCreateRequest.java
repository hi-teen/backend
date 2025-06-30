package backend.hiteen.board.dto.request;

import backend.hiteen.board.entity.Category;
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

    @NotNull(message = "카테고리를 선택해주세요")
    @Schema(
            description = "게시글 카테고리\n FREE:자유게시판 | SECRET:비밀게시판 | PROMOTION:홍보게시판 | INFORMATION:정보게시판 | GRADE1,2,3:학년게시판 ",
            example = "FREE", allowableValues = {"FREE", "SECRET","PROMOTION", "INFORMATION", "GRADE1", "GRADE2", "GRADE3"}
    )
    private Category category;

}





package backend.hiteen.board.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;

@Schema(description = "작성자 공개 여부")
public enum DisclosureStatus {
    @Schema(description = "실명")
    PUBLIC,
    @Schema(description = "익명")
    ANONYMOUS
}

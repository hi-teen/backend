package backend.hiteen.externalapi.timetable.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableDto implements Serializable {

    @Schema(description = "교시 번호(1교시, 2교시....)", example = "1")
    private int period;

    @Schema(description = "과목 명칭", example = "수학")
    private String subject;
}

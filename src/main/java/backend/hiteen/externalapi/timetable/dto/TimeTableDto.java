package backend.hiteen.externalapi.timetable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// TODO: 응답 데이터 예시 추가
public class TimeTableDto {

    private int period;
    private String subject;
}

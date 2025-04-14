package backend.hiteen.externalapi.timetable.controller;

import backend.hiteen.externalapi.timetable.dto.TimeTableDto;
import backend.hiteen.externalapi.timetable.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timetable")
@Tag(name = "TimeTable", description = "시간표 API")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @GetMapping
    @Operation(summary = "시간표 조회", description = "1학기 시간표를 조회합니다.")
    public ResponseEntity<Map<String, List<TimeTableDto>>> getTimeTable(@RequestParam String officeCode,
                                                                        @RequestParam String schoolCode,
                                                                        @RequestParam String grade,
                                                                        @RequestParam String classNum) throws IOException {
        return ResponseEntity.ok(timeTableService.getTimeTable(officeCode, schoolCode, grade, classNum)
        );
    }
}

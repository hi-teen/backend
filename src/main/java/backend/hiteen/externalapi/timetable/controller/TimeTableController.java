package backend.hiteen.externalapi.timetable.controller;

import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.externalapi.timetable.dto.TimeTableDto;
import backend.hiteen.externalapi.timetable.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<Map<String, List<TimeTableDto>>>> getTimeTable(
            @RequestParam @Schema(description = "교육청 코드", example = "B10") String officeCode,
            @RequestParam @Schema(description = "학교 코드", example = "7010117") String schoolCode,
            @RequestParam @Schema(description = "학년", example = "1") String grade,
            @RequestParam(name = "classNum") @Schema(description = "반", example = "3") String classNum) {

        Map<String, List<TimeTableDto>> timetable = timeTableService.getTimeTable(
                officeCode, schoolCode, grade, classNum);

        return ResponseEntity
                .status(SuccessCode.TIMETABLE_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.TIMETABLE_FETCHED, timetable));
    }
}

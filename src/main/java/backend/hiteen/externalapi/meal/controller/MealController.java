package backend.hiteen.externalapi.meal.controller;

import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.externalapi.meal.dto.MealDto;
import backend.hiteen.externalapi.meal.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/school-meal")
@Tag(name = "SchoolMeal", description = "급식표 API")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping
    @Operation(summary = "급식표 조회", description = "한달치 급식을 조회합니다.")
    public ResponseEntity<ApiResponse<Map<String, Map<String, MealDto>>>> getSchoolMeal(
            @RequestParam @Schema(description = "교육청 코드", example = "B10") String officeCode,
            @RequestParam @Schema(description = "학교 코드", example = "7010117") String schoolCode,
            @RequestParam @Schema(description = "년도", example = "2025") int year,
            @RequestParam @Schema(description = "월", example = "5") int month) {

        Map<String, Map<String, MealDto>> meals = mealService.getSchoolMeal(officeCode, schoolCode, year, month);
        return ResponseEntity
                .status(SuccessCode.MEAL_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.MEAL_FETCHED, meals));
    }
}

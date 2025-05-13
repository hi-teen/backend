package backend.hiteen.externalapi.meal.controller;

import backend.hiteen.externalapi.meal.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/school-meal")
@Tag(name = "SchoolMeal", description = "급식표 API")
// TODO: Swagger 문서 내 연동 API 설명 추가(NEIS API 기준 입력값 등)
// TODO: 응답 데이터 예시 추가
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping
    @Operation(summary = "급식표 조회", description = "1달치 급식을 조회합니다.")
    public ResponseEntity<Map<String, List<String>>> getSchoolMeal(@RequestParam String officeCode,
                                                               @RequestParam String schoolCode,
                                                               @RequestParam int year,
                                                               @RequestParam int month) {
        return ResponseEntity.ok(mealService.getSchoolMeal(officeCode, schoolCode, year, month));
    }

}

package backend.hiteen.externalapi.meal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MealService {

    private final WebClient neisWebClient;

    @Value("${openapi.api-key}")
    private String apiKey;

    public Map<String, List<String>> getSchoolMeal(String officeCode, String schoolCode, int year, int month) {

        Map<String,List<String>> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());

        String response =  neisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/mealServiceDietInfo")
                        .queryParam("KEY", apiKey)
                        .queryParam("Type", "json")
                        .queryParam("ATPT_OFCDC_SC_CODE", officeCode)
                        .queryParam("SD_SCHUL_CODE", schoolCode)
                        .queryParam("MLSV_FROM_YMD", from.format(formatter))
                        .queryParam("MLSV_TO_YMD", to.format(formatter))
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rows = objectMapper.readTree(response)
                    .path("mealServiceDietInfo").get(1).path("row");

            for (JsonNode row : rows) {
                String date = row.path("MLSV_YMD").asText(); // 날짜
                String rawMenu = row.path("DDISH_NM").asText();

                List<String> menus = Arrays.stream(rawMenu.split("<br/>"))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList();

                result.put(date, menus);
            }
        } catch (Exception e) {
            throw new RuntimeException("급식 데이터 읽기 실패", e);
        }

        return result;
    }
}

package backend.hiteen.externalapi.meal.service;

import backend.hiteen.externalapi.meal.dto.MealDto;
import backend.hiteen.externalapi.meal.exception.MealFetchFailedException;
import backend.hiteen.externalapi.meal.exception.MealNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MealService {

    private final WebClient neisWebClient;
    private final ObjectMapper objectMapper;

    @Value("${openapi.api-key}")
    private String apiKey;

    @Cacheable(value = "meal", key = "#officeCode + '_' + #schoolCode + '_' + #year + '_' + #month")
    public Map<String, Map<String, MealDto>> getSchoolMeal(String officeCode, String schoolCode, int year, int month)
    {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());

        String response = neisWebClient.get()
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
            JsonNode root = objectMapper.readTree(response);
            JsonNode infoNode = root.path("mealServiceDietInfo");
            if (!infoNode.isArray() || infoNode.size() < 2) {
                throw new MealNotFoundException();
            }
            JsonNode rowsNode = infoNode.get(1).path("row");
            if (rowsNode == null || !rowsNode.isArray() || rowsNode.isEmpty()) {
                throw new MealNotFoundException();
            }

            Map<String, Map<String, MealDto>> result = new LinkedHashMap<>();

            for (JsonNode row : rowsNode) {
                String date = row.path("MLSV_YMD").asText();
                String mealType = row.path("MMEAL_SC_NM").asText();
                String rawMenu = row.path("DDISH_NM").asText();
                String calories = row.path("CAL_INFO").asText();
                String nutrients = row.path("NTR_INFO").asText().replaceAll("<br/>", "\n");

                List<String> menus = Arrays.stream(rawMenu.split("<br/>"))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList();

                MealDto detail = new MealDto(menus, calories, nutrients);

                result.computeIfAbsent(date, d -> new LinkedHashMap<>())
                        .put(mealType, detail);
            }
            return result;
        } catch (IOException e) {
            throw new MealFetchFailedException();
        }
    }
}
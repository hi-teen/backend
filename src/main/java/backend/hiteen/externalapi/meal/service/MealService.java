package backend.hiteen.externalapi.meal.service;

import backend.hiteen.externalapi.meal.exception.MealFetchFailedException;
import backend.hiteen.externalapi.meal.exception.MealNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    public Map<String, List<String>> getSchoolMeal(String officeCode, String schoolCode, int year, int month) {

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
            JsonNode root = objectMapper.readTree(response);
            JsonNode infoNode = root.path("mealServiceDietInfo");
            if (!infoNode.isArray() || infoNode.size() < 2) {
                throw new MealNotFoundException();
            }
            JsonNode rowsNode = infoNode.get(1).path("row");
            if (rowsNode == null || !rowsNode.isArray() || rowsNode.isEmpty()) {
                throw new MealNotFoundException();
            }

            Map<String, List<String>> result = new LinkedHashMap<>();
            for (JsonNode row : rowsNode) {
                String date = row.path("MLSV_YMD").asText();
                String rawMenu = row.path("DDISH_NM").asText();
                List<String> menus = Arrays.stream(rawMenu.split("<br/>"))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList();
                result.put(date, menus);
            }
            return result;
        } catch (IOException e) {
            throw new MealFetchFailedException();
        }
    }
}

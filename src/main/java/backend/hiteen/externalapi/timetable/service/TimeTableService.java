package backend.hiteen.externalapi.timetable.service;

import backend.hiteen.externalapi.timetable.dto.TimeTableDto;
import backend.hiteen.externalapi.timetable.exception.TimeTableFetchFailedException;
import backend.hiteen.externalapi.timetable.exception.TimeTableNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final WebClient neisWebClient;
    private final ObjectMapper objectMapper;

    @Value("${openapi.api-key}")
    private String apiKey;

    public Map<String, List<TimeTableDto>> getTimeTable(
            String officeCode,
            String schoolCode,
            String grade,
            String classNum) {

        // 주 단위 월요일부터 금요일까지
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        Map<String, List<TimeTableDto>> weekly = new LinkedHashMap<>();

        for (int i = 0; i < 5; i++) {
            LocalDate date = monday.plusDays(i);
            String day = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

            String response = neisWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/hisTimetable")
                            .queryParam("KEY", apiKey)
                            .queryParam("Type", "json")
                            .queryParam("ATPT_OFCDC_SC_CODE", officeCode)
                            .queryParam("SD_SCHUL_CODE", schoolCode)
                            .queryParam("ALL_TI_YMD", date.format(fmt))
                            .queryParam("GRADE", grade)
                            .queryParam("CLASS_NM", classNum)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, client -> Mono.error(new TimeTableFetchFailedException()))
                    .bodyToMono(String.class)
                    .block();

            try {
                JsonNode root = objectMapper.readTree(response);
                JsonNode infoNode = root.path("hisTimetable");
                if (!infoNode.isArray() || infoNode.size() < 2) {
                    throw new TimeTableNotFoundException();
                }
                JsonNode rows = infoNode.get(1).path("row");
                if (rows == null || !rows.isArray() || rows.isEmpty()) {
                    throw new TimeTableNotFoundException();
                }

                List<TimeTableDto> dayList = new ArrayList<>();
                for (JsonNode row : rows) {
                    int period = row.path("PERIO").asInt();
                    String subject = row.path("ITRT_CNTNT").asText();
                    dayList.add(new TimeTableDto(period, subject));
                }
                weekly.put(day, dayList);
            } catch (IOException e) {
                throw new TimeTableFetchFailedException();
            }
        }
        return weekly;
    }
}

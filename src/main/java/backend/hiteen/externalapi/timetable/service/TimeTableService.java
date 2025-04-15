package backend.hiteen.externalapi.timetable.service;

import backend.hiteen.externalapi.timetable.dto.TimeTableDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Value("${openapi.api-key}")
    private String apiKey;

    public Map<String, List<TimeTableDto>> getTimeTable(String officeCode, String schoolCode, String grade, String classNum) throws IOException {

        Map<String, List<TimeTableDto>> weekely = new LinkedHashMap<>();
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (int i =0; i < 5; i++) {
            LocalDate date = monday.plusDays(i);
            String day = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN); //월욜

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
                    .bodyToMono(String.class)
                    .block();


            JsonNode rows = new ObjectMapper()
                    .readTree(response)
                    .path("hisTimetable").get(1).path("row");

            List<TimeTableDto> dayList = new ArrayList<>();
            for (JsonNode row : rows) {
                int period = row.path("PERIO").asInt();
                String subject = row.path("ITRT_CNTNT").asText();
                dayList.add(new TimeTableDto(period, subject));
            }

            weekely.put(day, dayList);
        }
        return  weekely;
    }
}

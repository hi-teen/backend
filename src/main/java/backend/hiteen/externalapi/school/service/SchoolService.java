package backend.hiteen.externalapi.school.service;

import backend.hiteen.externalapi.school.dto.SchoolSearchResponse;
import backend.hiteen.externalapi.school.entity.School;
import backend.hiteen.externalapi.school.exception.SchoolFetchException;
import backend.hiteen.externalapi.school.reporitory.SchoolRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final WebClient webClient;
    private final SchoolRepository schoolRepository;
    private final ObjectMapper objectMapper;

    @Value("${openapi.api-key}")
    private String apiKey;

    private static final String HIGH_SCHOOL = "고등학교";

    public int fetchAndSaveAllHighSchools() {
        int currentPage = 1;
        int totalSaved = 0;

        while (true) {
            int pageForRequest = currentPage;

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/schoolInfo")
                            .queryParam("KEY", apiKey)
                            .queryParam("Type", "json")
                            .queryParam("pIndex", pageForRequest)
                            .queryParam("pSize", 1000)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            try {
                JsonNode root = objectMapper.readTree(response);
                JsonNode schoolInfo = root.path("schoolInfo");

                if (!schoolInfo.isArray() || schoolInfo.size() < 2) {
                    break;
                }

                JsonNode rows = schoolInfo.get(1).path("row");
                if (rows == null || !rows.isArray() || rows.isEmpty()) {
                    break;
                }

                for (JsonNode row : rows) {
                    if (!HIGH_SCHOOL.equals(row.path("SCHUL_KND_SC_NM").asText())) {
                        continue;
                    }

                    String schoolCode = row.path("SD_SCHUL_CODE").asText();
                    if (schoolRepository.existsBySchoolCode(schoolCode)) {
                        continue;
                    }

                    School school = School.builder()
                            .schoolName(row.path("SCHUL_NM").asText())
                            .schoolCode(schoolCode)
                            .eduOfficeCode(row.path("ATPT_OFCDC_SC_CODE").asText())
                            .eduOfficeName(row.path("ATPT_OFCDC_SC_NM").asText())
                            .kind(HIGH_SCHOOL)
                            .schoolUrl(row.path("HMPG_ADRES").asText())
                            .build();

                    schoolRepository.save(school);
                    totalSaved++;
                }

                currentPage++;
            } catch (Exception e) {
                throw new SchoolFetchException("학교 데이터 파싱 실패", e);
            }
        }

        return totalSaved;
    }

    public List<SchoolSearchResponse> searchHighSchoolsByName(String keyword) {
        return schoolRepository.findBySchoolNameContainingAndKind(keyword, "고등학교")
                .stream()
                .map(s -> new SchoolSearchResponse(s.getId(),
                                                   s.getSchoolName(),
                                                   s.getEduOfficeName()
                ))
                .toList();
    }

}
package backend.hiteen.externalapi.school.controller;

import backend.hiteen.externalapi.school.dto.SchoolSearchResponse;
import backend.hiteen.externalapi.school.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;


    @PostMapping("/init")
    public ResponseEntity<String> schools() {
        int savedCount = schoolService.fetchAndSaveAllHighSchools();
        return ResponseEntity.ok("총 " + savedCount + "개의 고등학교가 저장되었습니다.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<SchoolSearchResponse>> searchSchools(@RequestParam String keyword) {
        return ResponseEntity.ok(schoolService.searchHighSchoolsByName(keyword));
    }

}

package backend.hiteen.scrap.controller;

import backend.hiteen.scrap.dto.ScrapBoardResponse;
import backend.hiteen.scrap.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scraps")
@RequiredArgsConstructor
@Tag(name = "Scrap", description = "스크랩 API")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    @Operation(summary = "스크랩", description = "사용자가 게시글을 스크랩/스크랩 취소 합니다.")
    public ResponseEntity<String> scrapBoard(@AuthenticationPrincipal String email, @RequestParam Long boardId){
        String message=scrapService.updateScrapBoard(email,boardId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/me")
    @Operation(summary = "내가 스크랩 한 게시글 전체 조회", description = "사용자가 스크랩 한 게시글을 전체 조회합니다.")
    public ResponseEntity<List<ScrapBoardResponse>> getMyScrapedBoards(@AuthenticationPrincipal String email){
        List<ScrapBoardResponse> responses=scrapService.getMyScrapedBoards(email);
        return ResponseEntity.ok(responses);
    }
}

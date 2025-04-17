package backend.hiteen.scrap.controller;

import backend.hiteen.scrap.dto.ScrapBoardResponse;
import backend.hiteen.scrap.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Scrap", description = "스크랩 API")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/members/{memberId}/scraps/boards/{boardId}")
    @Operation(summary = "스크랩", description = "사용자가 게시글을 스크랩/스크랩 취소 합니다.")
    public ResponseEntity<String> scrapBoard(@PathVariable Long memberId, @PathVariable Long boardId){
        String message=scrapService.updateScrapBoard(memberId,boardId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/members/{memberId}/scraps/boards")
    @Operation(summary = "내가 스크랩 한 게시글 전체 조회", description = "사용자가 스크랩 한 게시글을 전체 조회합니다.")
    public ResponseEntity<List<ScrapBoardResponse>> getMyScrapedBoards(@PathVariable Long memberId){
        List<ScrapBoardResponse> responses=scrapService.getMyScrapedBoards(memberId);
        return ResponseEntity.ok(responses);
    }
}

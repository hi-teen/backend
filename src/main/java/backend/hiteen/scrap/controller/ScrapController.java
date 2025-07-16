package backend.hiteen.scrap.controller;

import backend.hiteen.auth.security.CustomUserPrincipal;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import backend.hiteen.scrap.dto.ScrapBoardResponse;
import backend.hiteen.scrap.entity.ScrapActionResult;
import backend.hiteen.scrap.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scraps")
@RequiredArgsConstructor
@Tag(name = "Scrap", description = "스크랩 API")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    @Operation(summary = "스크랩", description = "사용자가 게시글을 스크랩/스크랩 취소 합니다.")
    public ResponseEntity<String> scrapBoard(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestParam Long boardId){
        String email = principal.getUsername();

        if (result==ScrapActionResult.CREATED){
            return ResponseEntity.status(SuccessCode.SCRAP_CREATED.getStatus()).body(ApiResponse.success(SuccessCode.SCRAP_CREATED));
        } else{
            return ResponseEntity.status(SuccessCode.SCRAP_DELETED.getStatus()).body(ApiResponse.success(SuccessCode.SCRAP_DELETED));
        }
    }

    @GetMapping("/my")
    @Operation(summary = "내가 스크랩 한 게시글 전체 조회", description = "사용자가 스크랩 한 게시글을 전체 조회합니다.")
    public ResponseEntity<List<ScrapBoardResponse>> getMyScrapedBoards(@AuthenticationPrincipal CustomUserPrincipal principal){
        String email = principal.getUsername();
        List<ScrapBoardResponse> responses=scrapService.getMyScrapedBoards(email);
        return ResponseEntity.status(SuccessCode.SCRAPED_BOARDS_FETCHED.getStatus()).body(ApiResponse.success(SuccessCode.SCRAPED_BOARDS_FETCHED, responses));
    }
}

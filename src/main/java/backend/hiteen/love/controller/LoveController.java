package backend.hiteen.love.controller;

import backend.hiteen.love.dto.LoveBoardResponse;
import backend.hiteen.love.service.LoveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loves")
@Tag(name = "Love", description = "좋아요 API")
public class LoveController {

    private final LoveService loveService;

    @PostMapping
    @Operation(summary = "좋아요", description = "사용자가 좋아요를 추가하거나 취소합니다.")
    public ResponseEntity<String> loveBoard(@AuthenticationPrincipal String email, @RequestParam Long boardId){
        String message=loveService.updateLoveBoard(email,boardId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/me")
    @Operation(summary = "내가 좋아요 한 게시글 전체 조회", description = "사용자가 좋아요 한 게시글을 전체 조회합니다.")
    public ResponseEntity<List<LoveBoardResponse>> getMyLovedBoards(@AuthenticationPrincipal String email){
        List<LoveBoardResponse> responses=loveService.getMyLovedBoards(email);
        return ResponseEntity.ok(responses);
    }
}

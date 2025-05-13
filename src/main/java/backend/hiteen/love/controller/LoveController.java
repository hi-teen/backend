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

//TODO: 엔드포인트 공통으로 시작 'loves'는 @RequestMappint("/loves")로 사용할 것
//FIXME: 내가 좋아요 한 게시글 조회는 토큰으로 넘겨서 엔드포인트에는 memberId 뺄 것 (+ memberId PathVariable 제거)

@RestController
@RequiredArgsConstructor
@RequestMapping("/loves")
@Tag(name = "Love", description = "좋아요 API")
public class LoveController {

    private final LoveService loveService;

    @PostMapping
    @Operation(summary = "좋아요", description = "사용자가 좋아요를 추가하거나 취소합니다.")
    public ResponseEntity<String> loveBoard(@AuthenticationPrincipal String email, @RequestParam Long boardId){
        String message=loveService.updateLoveBoard(email,boardId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/my")
    @Operation(summary = "내가 좋아요 한 게시글 전체 조회", description = "사용자가 좋아요 한 게시글을 전체 조회합니다.")
    public ResponseEntity<List<LoveBoardResponse>> getMyLovedBoards(@AuthenticationPrincipal String email){
        List<LoveBoardResponse> responses=loveService.getMyLovedBoards(email);
        return ResponseEntity.ok(responses);
    }
}

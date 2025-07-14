package backend.hiteen.board.controller;


import backend.hiteen.auth.security.CustomUserPrincipal;
import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
@Tag(name = "Board", description = "게시글 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "게시글 추가", description = "사용자가 게시글을 작성합니다.")
    public ResponseEntity<BoardResponse> createBoard(@AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody BoardCreateRequest request){
        BoardResponse response=boardService.createBoard(principal.getUsername(),request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/boards")
    @Operation(summary = "전체 게시글 목록 조회", description = "모든 사용자가 작성한 게시글을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getAllBoards(){
        List<BoardResponse> responses=boardService.getAllBoards();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 통해 특정 게시글의 상세 내용을 조회합니다.")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getBoardById(boardId);
        return ResponseEntity.ok(boardResponse);
    }

    @GetMapping("/my")
    @Operation(summary = "내가 작성한 게시글 목록 조회", description = "사용자가 자신이 작성한 게시글 목록을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getAllMyBoards(@AuthenticationPrincipal CustomUserPrincipal principal){
        List<BoardResponse> responses=boardService.getAllMyBoards(principal.getUsername());
        return ResponseEntity.ok(responses);
    }

}

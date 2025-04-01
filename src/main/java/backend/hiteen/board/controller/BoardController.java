package backend.hiteen.board.controller;


import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Board", description = "게시글 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    @Operation(summary = "게시글 추가", description = "사용자가 게시글을 작성합니다.")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request){
        BoardResponse response=boardService.createBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/board")
    @Operation(summary = "게시글 전체 조회", description = "사용자가 모든 게시글을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getAllBoards(){
        List<BoardResponse> responses=boardService.getAllBoards();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/board/{boardId}")
    @Operation(summary = "게시글 단일 조회", description = "사용자가 특정 게시글을 조회합니다.")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getBoardById(boardId);
        return ResponseEntity.ok(boardResponse);
    }

    @GetMapping("/member/{memberId}/board")
    @Operation(summary = "내가 쓴 글 전체 조회", description = "사용자가 작성한 전체 게시글을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getAllMyBoards(@PathVariable Long memberId){
        List<BoardResponse> responses=boardService.getMyBoards(memberId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/member/{memberId}/board/{boardId}")
    @Operation(summary = "내가 쓴 글 단일 조회", description = "사용자가 작성한 특정 게시글을 조회합니다.")
    public ResponseEntity<BoardResponse> getMyBoardDetail(@PathVariable Long memberId, @PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getMyBoardDetail(memberId, boardId);
        return ResponseEntity.ok(boardResponse);
    }

    @GetMapping("/member/{memberId}/board/love")
    @Operation(summary = "내가 좋아요 한 게시글 전체 조회", description ="사용자가 좋아요 한 게시글을 모두 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getMyLovedBoards(@PathVariable Long memberId){
        List<BoardResponse> responses=boardService.getMyLovedBoard(memberId);
        return ResponseEntity.ok(responses);
    }
}

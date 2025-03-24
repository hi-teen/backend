package backend.hiteen.board.controller;


import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "Board", description = "게시글 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request){
        BoardResponse response=boardService.createBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/board")
    public ResponseEntity<List<BoardResponse>> getAllBoards(){
        List<BoardResponse> responses=boardService.getAllBoards();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getBoardById(boardId);
        return ResponseEntity.ok(boardResponse);
    }

    @GetMapping("/member/{memberId}/board")
    public ResponseEntity<List<BoardResponse>> getAllMyBoards(@PathVariable Long memberId){
        List<BoardResponse> responses=boardService.getMyBoards(memberId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/member/{memberId}/board/{boardId}")
    public ResponseEntity<BoardResponse> getMyBoardDetail(@PathVariable Long memberId, @PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getMyBoardDetail(memberId, boardId);
        return ResponseEntity.ok(boardResponse);
    }
}

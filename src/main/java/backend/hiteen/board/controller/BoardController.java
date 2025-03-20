package backend.hiteen.board.controller;


import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request){
        BoardResponse response=boardService.createBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAllBoards(){
        List<BoardResponse> responses=boardService.getAllBoards();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getBoardById(boardId);
        return ResponseEntity.ok(boardResponse);
    }
}

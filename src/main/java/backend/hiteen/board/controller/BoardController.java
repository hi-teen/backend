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

//TODO: 엔드포인트 공통으로 시작 'boards'는 @RequestMappint("/boards")로 사용할 것
//FIXME: 내가 쓴 글 조회는 토큰으로 넘겨서 엔드포인트에는 memberId 뺄 것 (+ memberId PathVariable 제거)
@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Board", description = "게시글 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    @Operation(summary = "게시글 추가", description = "사용자가 게시글을 작성합니다.")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request){
        BoardResponse response=boardService.createBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     FIXME: description 설명 모호함.
     getAllBoards와 getMyBoards의 Swagger 설명이 혼동됨.
     → '전체 게시글 조회' vs '내가 쓴 게시글 조회' 구분 필요.
     getBoardById도 동일
    */
    @GetMapping("/boards")
    @Operation(summary = "게시글 전체 조회", description = "사용자가 모든 게시글을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getAllBoards(){
        List<BoardResponse> responses=boardService.getAllBoards();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 단일 조회", description = "사용자가 특정 게시글을 조회합니다.")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getBoardById(boardId);
        return ResponseEntity.ok(boardResponse);
    }

    @GetMapping("/members/{memberId}/boards")
    @Operation(summary = "내가 쓴 글 전체 조회", description = "사용자가 작성한 전체 게시글을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getAllMyBoards(@PathVariable Long memberId){
        List<BoardResponse> responses=boardService.getMyBoards(memberId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/members/{memberId}/boards/{boardId}")
    @Operation(summary = "내가 쓴 글 단일 조회", description = "사용자가 작성한 특정 게시글을 조회합니다.")
    public ResponseEntity<BoardResponse> getMyBoardDetail(@PathVariable Long memberId, @PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getMyBoardDetail(memberId, boardId);
        return ResponseEntity.ok(boardResponse);
    }

}

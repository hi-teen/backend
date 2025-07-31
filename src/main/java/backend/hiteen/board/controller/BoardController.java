package backend.hiteen.board.controller;


import backend.hiteen.auth.security.CustomUserPrincipal;
import backend.hiteen.board.dto.request.BoardCreateRequest;
import backend.hiteen.board.dto.response.BoardResponse;
import backend.hiteen.board.service.BoardService;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                                 @Valid @RequestBody BoardCreateRequest request){
        BoardResponse response=boardService.createBoard(principal.getUsername(),request);
        return ResponseEntity
                .status(SuccessCode.BOARD_CREATED.getStatus())
                .body(ApiResponse.success(SuccessCode.BOARD_CREATED,response));
    }


    @GetMapping
    @Operation(summary = "전체 게시글 목록 조회", description = "본인 학교의 게시글 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getAllBoards(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        List<BoardResponse> responses=boardService.getAllBoards(principal.getId());
        return ResponseEntity
                .status(SuccessCode.BOARD_ALL_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.BOARD_ALL_FETCHED, responses));
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 통해 특정 게시글의 상세 내용을 조회합니다.")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardById(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long boardId){
        BoardResponse boardResponse=boardService.getBoardById(boardId, principal.getId());
        return ResponseEntity
                .status(SuccessCode.BOARD_DETAIL_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.BOARD_DETAIL_FETCHED,boardResponse));
    }

    @GetMapping("/me")
    @Operation(summary = "내가 작성한 게시글 목록 조회", description = "사용자가 자신이 작성한 게시글 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getAllMyBoards(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Long memberId = principal.getId();
        List<BoardResponse> responses = boardService.getAllMyBoards(memberId);
        return ResponseEntity
                .status(SuccessCode.MY_BOARD_LIST_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.MY_BOARD_LIST_FETCHED, responses));
    }


    @GetMapping("/popular")
    @Operation(summary ="인기게시글 목록 조회", description = "인기게시글을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getPopularBoards(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        List<BoardResponse> responses=boardService.getPopularBoards(principal.getId());
        return ResponseEntity
                .status(SuccessCode.POPULAR_BOARD_ALL_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.POPULAR_BOARD_ALL_FETCHED, responses));
    }

    @GetMapping("/search")
    @Operation(summary ="게시글 검색", description = "게시글을 검색합니다.")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> searchBoards(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam("keyword") String keyword){

        List<BoardResponse> responses=boardService.searchBoards(keyword, principal.getId());
        return ResponseEntity
                .status(SuccessCode.SEARCHED_BOARD_LIST_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.SEARCHED_BOARD_LIST_FETCHED, responses));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long boardId
    ){
        boardService.deleteBoard(principal.getUsername(),boardId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.BOARD_DELETED));
    }

}

package backend.hiteen.comment.controller;

import backend.hiteen.auth.security.CustomUserPrincipal;
import backend.hiteen.comment.dto.request.CommentRequestDto;
import backend.hiteen.comment.dto.request.ReplyCommentRequestDto;
import backend.hiteen.comment.dto.response.CommentLikeResponse;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.dto.response.MyCommentResponse;
import backend.hiteen.comment.dto.response.ReplyCommentResponseDto;
import backend.hiteen.comment.service.CommentService;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    public ResponseEntity<ApiResponse<CommentResponseDto>> addComment(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.addComment(
                principal.getId(),
                request.getBoardId(),
                request.getContent()
        );

        return ResponseEntity
                .status(SuccessCode.COMMENT_CREATED.getStatus())
                .body(ApiResponse.success(SuccessCode.COMMENT_CREATED, response));
    }

    @PostMapping("/{commentId}/replies")
    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대한 대댓글을 작성합니다.")
    public ResponseEntity<ApiResponse<ReplyCommentResponseDto>> addReplyComment(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long commentId,
            @RequestBody ReplyCommentRequestDto request) {
        ReplyCommentResponseDto response = commentService.addReplyComment(
                principal.getId(),
                commentId,
                request.getContent()
                );

        return ResponseEntity
                .status(SuccessCode.REPLY_CREATED.getStatus())
                .body(ApiResponse.success(SuccessCode.REPLY_CREATED, response));
    }

    @GetMapping("/board/{boardId}")
    @Operation(summary = "댓글 조회", description = "게시글에 달린 댓글과 대댓글을 모두 조회합니다.")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComments(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long boardId) {
        Long memberId = principal != null ? principal.getId() : null;
        List<CommentResponseDto> comments = commentService.getComments(boardId, memberId);

        return ResponseEntity
                .status(SuccessCode.COMMENT_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.COMMENT_FETCHED, comments));
    }

    @GetMapping("/me")
    @Operation(summary = "내가 쓴 댓글/대댓글 조회", description = "내가 쓴 댓글/대댓글과 게시글 정보를 조회합니다")
    public ResponseEntity<ApiResponse<List<MyCommentResponse>>> getMyComments(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<MyCommentResponse> comments = commentService.getMyComments(principal.getId());
        return ResponseEntity
                .status(SuccessCode.COMMENT_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.COMMENT_FETCHED, comments));
    }

    @PostMapping({"/{commentId}/like"})
    @Operation(summary = "댓글 좋아요", description = "댓글, 대댓글 좋아요")
    public ResponseEntity<ApiResponse<CommentLikeResponse>> toggleLike(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long commentId
    ) {
        CommentLikeResponse response = commentService.toggleLike(commentId, principal.getId());
        SuccessCode code = response.isLiked() ?
                SuccessCode.COMMENT_LIKED_CREATED :
                SuccessCode.COMMENT_LIKED_DELETED;

        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.success(code, response));
    }
}

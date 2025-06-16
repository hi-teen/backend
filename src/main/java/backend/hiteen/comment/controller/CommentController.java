package backend.hiteen.comment.controller;

import backend.hiteen.comment.dto.request.CommentRequestDto;
import backend.hiteen.comment.dto.request.ReplyCommentRequestDto;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.service.CommentService;
import backend.hiteen.common.response.ApiResponse;
import backend.hiteen.common.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
//TODO: @Operation(summary, description) 추가
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    public ResponseEntity<ApiResponse<CommentResponseDto>> addComment(
            @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.addComment(request);

        return ResponseEntity
                .status(SuccessCode.COMMENT_CREATED.getStatus())
                .body(ApiResponse.success(SuccessCode.COMMENT_CREATED, response));
    }

    @PostMapping("/{commentId}/replies")
    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대한 대댓글을 작성합니다.")
    public ResponseEntity<ApiResponse<CommentResponseDto>> addReplyComment(
            @PathVariable Long commentId,
            @RequestBody ReplyCommentRequestDto request) {
        CommentResponseDto response = commentService.addReplyComment(commentId, request);

        return ResponseEntity
                .status(SuccessCode.REPLY_CREATED.getStatus())
                .body(ApiResponse.success(SuccessCode.REPLY_CREATED, response));
    }

    @GetMapping("/board/{boardId}")
    @Operation(summary = "댓글 조회", description = "게시글에 달린 댓글과 대댓글을 모두 조회합니다.")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComments(@PathVariable Long boardId) {
        List<CommentResponseDto> comments = commentService.getComments(boardId);

        return ResponseEntity
                .status(SuccessCode.COMMENT_FETCHED.getStatus())
                .body(ApiResponse.success(SuccessCode.COMMENT_FETCHED, comments));
    }

}

package backend.hiteen.comment.controller;

import backend.hiteen.comment.dto.request.CommentRequestDto;
import backend.hiteen.comment.dto.request.ReplyCommentRequestDto;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
//TODO: @Operation(summary, description) 추가
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestBody CommentRequestDto request) {
        commentService.addComment(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{commentId}/replies")
    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대한 대댓글을 작성합니다.")
    public ResponseEntity<CommentResponseDto> addReplyComment(
            @PathVariable Long commentId,
            @RequestBody ReplyCommentRequestDto request) {
        commentService.addReplyComment(commentId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/board/{boardId}")
    @Operation(summary = "댓글 조회", description = "게시글에 달린 댓글과 대댓글을 모두 조회합니다.")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long boardId) {
        List<CommentResponseDto> comments = commentService.getComments(boardId);
        return ResponseEntity.ok(comments);
    }

}

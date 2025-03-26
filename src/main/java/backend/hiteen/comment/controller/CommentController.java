package backend.hiteen.comment.controller;

import backend.hiteen.comment.dto.request.CommentRequestDto;
import backend.hiteen.comment.dto.request.ReplyCommentRequestDto;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestBody CommentRequestDto request) {
        commentService.addComment(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("comments/{commentId}/replies")
    public ResponseEntity<CommentResponseDto> addReplyComment(
            @PathVariable Long commentId,
            @RequestBody ReplyCommentRequestDto request) {
        commentService.addReplyComment(commentId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/board/{boardId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long boardId) {
        List<CommentResponseDto> comments = commentService.getComments(boardId);
        return ResponseEntity.ok(comments);
    }

}

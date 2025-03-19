package backend.hiteen.comment.controller;

import backend.hiteen.comment.dto.request.CommentRequestDto;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestBody CommentRequestDto request) {
        commentService.addComment(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

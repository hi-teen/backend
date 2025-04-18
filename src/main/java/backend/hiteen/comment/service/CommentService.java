package backend.hiteen.comment.service;
import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.comment.dto.request.CommentRequestDto;
import backend.hiteen.comment.dto.request.ReplyCommentRequestDto;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.dto.response.ReplyCommentResponseDto;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    public final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 등록
    @Transactional
    public void addComment(CommentRequestDto requestDto) {
        // TODO: 멤버 존재 여부 검증 로직 추가 예정(멤버 기능 완료 시 반영)

        Board board = boardRepository.findById(requestDto.getBoardId())
                .orElseThrow();
        int nextAnonNumber = nextAnonymousNumber(board);

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .anonymousNumber(nextAnonNumber)
                .board(board)
                .build();
        commentRepository.save(comment);
    }

    @Transactional
    public void addReplyComment(Long commentId, ReplyCommentRequestDto replyRequest) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        Board board = parentComment.getBoard();

        int nextAnonNumber = nextAnonymousNumber(board);

        Comment replycomment = Comment.builder()
                .content(replyRequest.getContent())
                .board(parentComment.getBoard())
                .parentComment(parentComment)
                .anonymousNumber(nextAnonNumber)
                .build();

        commentRepository.save(replycomment);
    }

    private int nextAnonymousNumber(Board board) {
        Integer maxAnonNumber = commentRepository.findMaxAnonymousNumberByBoardId(board.getId());
        return (maxAnonNumber == null) ? 1 : maxAnonNumber +1;
    }


    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }

        List<Comment> topLevelComments = commentRepository.findRootsByBoardId(boardId);

        return topLevelComments.stream()
                .map(this::covertToDto)
                .toList();
    }

    private CommentResponseDto covertToDto(Comment comment) {
        List<ReplyCommentResponseDto> replies = comment.getChildrenComment().stream()
                .map(child -> new ReplyCommentResponseDto(
                        child.getId(),
                        child.getContent(),
                        child.getAnonymousNumber()))
                .toList();

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getAnonymousNumber(),
                replies);
    }


    }



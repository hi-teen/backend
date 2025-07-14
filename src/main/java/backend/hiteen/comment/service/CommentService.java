package backend.hiteen.comment.service;
import backend.hiteen.board.entity.Board;
import backend.hiteen.board.exception.BoardNotFoundException;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.dto.response.ReplyCommentResponseDto;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.exception.CommentNotFoundException;
import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.MemberNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    public final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 댓글 등록
    @Transactional
    public CommentResponseDto addComment(Long memberId, Long boardId, String content) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        int nextAnonNumber = nextAnonymousNumber(board);

        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .anonymousNumber(nextAnonNumber)
                .build();
        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getAnonymousNumber(),
                comment.getCreatedAt(),
                List.of()
        );
    }

    @Transactional
    public CommentResponseDto addReplyComment(Long memberId,Long parentCommentId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentNotFoundException::new);

        Board board = parentComment.getBoard();
        int nextAnonNumber = nextAnonymousNumber(board);

        Comment replycomment = Comment.builder()
                .member(member)
                .board(board)
                .parentComment(parentComment)
                .content(content)
                .anonymousNumber(nextAnonNumber)
                .build();

        commentRepository.save(replycomment);

        return new CommentResponseDto(
                replycomment.getId(),
                replycomment.getContent(),
                replycomment.getAnonymousNumber(),
                replycomment.getCreatedAt(),
                List.of()
        );
    }

    private int nextAnonymousNumber(Board board) {
        Integer maxAnonNumber = commentRepository.findMaxAnonymousNumberByBoardId(board.getId());
        return (maxAnonNumber == null) ? 1 : maxAnonNumber +1;
    }


    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardNotFoundException();
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
                        child.getAnonymousNumber(),
                        child.getCreatedAt()
                        ))
                .toList();

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getAnonymousNumber(),
                comment.getCreatedAt(),
                replies);
    }


    }



package backend.hiteen.comment.service;
import backend.hiteen.board.entity.Board;
import backend.hiteen.board.exception.BoardNotFoundException;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.comment.dto.response.CommentLikeResponse;
import backend.hiteen.comment.dto.response.CommentResponseDto;
import backend.hiteen.comment.dto.response.MyCommentResponse;
import backend.hiteen.comment.dto.response.ReplyCommentResponseDto;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.entity.CommentLike;
import backend.hiteen.comment.exception.CommentNotFoundException;
import backend.hiteen.comment.exception.CommentNotOwnerException;
import backend.hiteen.comment.repository.CommentLikeRepository;
import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.member.MemberNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    public final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 등록
    @Transactional
    public CommentResponseDto addComment(Long memberId, Long boardId, String content) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Board board = boardRepository.findByIdWithPessimisticLock(boardId)
                .orElseThrow(BoardNotFoundException::new);

        validateSameSchool(member, board.getMember());

        int nextAnonNumber = nextAnonymousNumber(board);

        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .anonymousNumber(nextAnonNumber)
                .build();
        commentRepository.save(comment);
        
        // 댓글 카운트 증가
        board.increaseCommentCount();

        boolean isBoardWriter = comment.getMember().getId().equals(board.getMember().getId());

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getAnonymousNumber(),
                comment.getCreatedAt(),
                0,
                false,
                isBoardWriter,
                List.of()
        );
    }

    @Transactional
    public ReplyCommentResponseDto addReplyComment(Long memberId,Long parentCommentId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentNotFoundException::new);

        Board board = parentComment.getBoard();

        validateSameSchool(member, board.getMember());

        int nextAnonNumber = nextAnonymousNumber(board);

        Comment replycomment = Comment.builder()
                .member(member)
                .board(board)
                .parentComment(parentComment)
                .content(content)
                .anonymousNumber(nextAnonNumber)
                .build();

        commentRepository.save(replycomment);
        
        // 댓글 카운트 증가 (대댓글도 포함)
        board.increaseCommentCount();

        boolean replyIsBoardWriter = replycomment.getMember().getId().equals(board.getMember().getId());

        return new ReplyCommentResponseDto(
                replycomment.getId(),
                replycomment.getContent(),
                replycomment.getAnonymousNumber(),
                replycomment.getCreatedAt(),
                0,
                false,
                replyIsBoardWriter
        );
    }

    private int nextAnonymousNumber(Board board) {
        Integer maxAnonNumber = commentRepository.findMaxAnonymousNumberByBoardId(board.getId());
        return (maxAnonNumber == null) ? 1 : maxAnonNumber +1;
    }


    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getComments(Long boardId, Long memberId, Pageable pageable) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        validateSameSchool(member, board.getMember());

        Page<Comment> topLevelComments = commentRepository.findRootsByBoardId(boardId, pageable);

        return topLevelComments
                .map(c -> covertToDto(c, member));
    }

    @Transactional(readOnly = true)
    public List<MyCommentResponse> getMyComments(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<Comment> myComments = commentRepository.findAllByMember(member);

        return myComments.stream()
                .map(comment -> new MyCommentResponse(
                        comment.getBoard().getId(),
                        comment.getBoard().getTitle(),
                        comment.getId(),
                        comment.getContent(),
                        comment.getParentComment() != null,
                        comment.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public CommentLikeResponse toggleLike(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        validateSameSchool(member, comment.getBoard().getMember());

        Optional<CommentLike> optionalLike = commentLikeRepository.findByCommentAndMember(comment, member);

        boolean liked;
        if (optionalLike.isPresent()) {
            commentLikeRepository.deleteByCommentAndMember(comment, member);
            liked = false;
        } else {
            commentLikeRepository.save(CommentLike.builder()
                                               .comment(comment)
                                               .member(member)
                                               .build());
            liked = true;
        }

        int likeCount = commentLikeRepository.countByComment(comment);
        return new CommentLikeResponse(likeCount, liked);
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getMember().getId().equals(memberId)) {
            throw new CommentNotOwnerException();
        }
        
        Board board = comment.getBoard();
        
        // 삭제될 댓글 수 계산 (본 댓글 + 모든 대댓글)
        int deletedCommentsCount = 1 + comment.getChildrenComment().size();
        
        // 댓글 삭제 시 카운트 감소
        for (int i = 0; i < deletedCommentsCount; i++) {
            board.decreaseCommentCount();
        }

        commentRepository.delete(comment);
    }

    private CommentResponseDto covertToDto(Comment comment, Member member) {
        int likeCount = commentLikeRepository.countByComment(comment);
        boolean likedByMe = member != null && commentLikeRepository.findByCommentAndMember(comment, member).isPresent();
        boolean isBoardWriter = comment.getMember().getId().equals(comment.getBoard().getMember().getId());

        List<ReplyCommentResponseDto> replies = comment.getChildrenComment().stream()
                .map(child -> {
                    int replyLikeCount = commentLikeRepository.countByComment(child);
                    boolean replyLikedByMe = member != null && commentLikeRepository.findByCommentAndMember(child, member).isPresent();
                    boolean replyIsBoardWriter = child.getMember().getId().equals(child.getBoard().getMember().getId());

                    return new ReplyCommentResponseDto(
                            child.getId(),
                            child.getContent(),
                            child.getAnonymousNumber(),
                            child.getCreatedAt(),
                            replyLikeCount,
                            replyLikedByMe,
                            replyIsBoardWriter
                    );
                })
                .toList();

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getAnonymousNumber(),
                comment.getCreatedAt(),
                likeCount,
                likedByMe,
                isBoardWriter,
                replies
                );
    }

    private void validateSameSchool(Member a, Member b) {
        if (!a.getSchool().getId().equals(b.getSchool().getId())) {
            throw new CommentNotOwnerException();
        }
    }

}



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
import backend.hiteen.comment.repository.CommentLikeRepository;
import backend.hiteen.comment.repository.CommentRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.MemberNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
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
        int nextAnonNumber = nextAnonymousNumber(board);

        Comment replycomment = Comment.builder()
                .member(member)
                .board(board)
                .parentComment(parentComment)
                .content(content)
                .anonymousNumber(nextAnonNumber)
                .build();

        commentRepository.save(replycomment);

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
    public List<CommentResponseDto> getComments(Long boardId, Long memberId) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardNotFoundException();
        }

        List<Comment> topLevelComments = commentRepository.findRootsByBoardId(boardId);
        Member member = memberId == null ? null : memberRepository.findById(memberId)
                .orElse(null);

        return topLevelComments.stream()
                .map(c -> covertToDto(c, member))
                .toList();
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
    }



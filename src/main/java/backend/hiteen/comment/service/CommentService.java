package backend.hiteen.comment.service;
import backend.hiteen.board.entity.Board;
import backend.hiteen.board.repository.BoardRepository;
import backend.hiteen.comment.dto.request.CommentRequestDto;
import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentService {

    public final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 등록
    @Transactional
    public void addComment(CommentRequestDto requestDto) {
        // TODO: 멤버 존재 여부 검증 로직 추가 예정(멤버 기능 완료 시 반영)

        // TODO: 게시글 검증은 게시글 기능 완료 시 로직 수정할 것(현재는 null 허용해 테스트 중)
        Board board = boardRepository.findById(requestDto.getBoardId()).orElse(null);


        Integer maxAnonNumber = commentRepository.findMaxAnonymousNumberByBoardId(requestDto.getBoardId());
        int nextAnonNumber = (maxAnonNumber == null) ? 1 : maxAnonNumber + 1;

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .anonymousNumber(nextAnonNumber)
                .board(board)
                .build();
        commentRepository.save(comment);
    }


    }



package backend.hiteen.board.repository;

import backend.hiteen.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByMemberId(Long memberId);
    Optional<Board> findByMemberIdAndId(Long memberId, Long boardId);
}

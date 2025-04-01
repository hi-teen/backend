package backend.hiteen.board.repository;

import backend.hiteen.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByMemberId(Long memberId);
    Optional<Board> findByMemberIdAndId(Long memberId, Long boardId);

    @Query("SELECT b FROM Board b JOIN b.loves l WHERE l.member.id=:memberId")
    List<Board> findLikedBoardByMemberId(Long memberId);

    @Query("SELECT b FROM Board b JOIN b.scraps s where s.member.id=:memberId")
    List<Board> findScrapedBoardByMemberId(Long memberId);

}
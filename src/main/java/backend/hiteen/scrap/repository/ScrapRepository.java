package backend.hiteen.scrap.repository;

import backend.hiteen.board.entity.Board;
import backend.hiteen.member.entity.Member;
import backend.hiteen.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long>{
    Optional<Scrap> findByMemberAndBoard(Member member, Board board);

    @Query("SELECT s.board FROM Scrap s WHERE s.member.id=:memberId")
    List<Board> findScrapedBoardsByMemberId(Long memberId);
}

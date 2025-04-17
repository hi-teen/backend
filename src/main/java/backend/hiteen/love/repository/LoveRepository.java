package backend.hiteen.love.repository;

import backend.hiteen.board.entity.Board;
import backend.hiteen.love.entity.Love;
import backend.hiteen.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love,Long> {
    Optional<Love> findByMemberAndBoard(Member member, Board board);

    @Query("SELECT l.board FROM Love l WHERE l.member.id=:memberId")
    List<Board> findLovedBoardsByMemberId(Long memberId);
}

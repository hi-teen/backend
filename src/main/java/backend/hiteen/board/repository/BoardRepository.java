package backend.hiteen.board.repository;

import backend.hiteen.board.entity.Board;
import backend.hiteen.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByMember(Member member);
    @Query("SELECT board FROM Board board WHERE (board.viewCount+board.loveCount)>=30")
    List<Board> findPopularBoards();

}
package backend.hiteen.board.repository;

import backend.hiteen.board.entity.Board;
import backend.hiteen.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByMember_School_Id(Long schoolId);

    List<Board> findAllByMemberAndMember_School_Id(Member member, Long schoolId);

    @Query("SELECT board FROM Board board WHERE (board.viewCount + board.loveCount) >= 30 AND board.member.school.id = :schoolId")
    List<Board> findPopularBoardsBySchool(Long schoolId);

    @Query("""
    SELECT board FROM Board board
    WHERE board.member.school.id = :schoolId
      AND (
           board.title LIKE %:keyword%
        OR board.content LIKE %:keyword%
        OR board.member.name LIKE %:keyword%
      )
    ORDER BY board.createdAt DESC
""")
    List<Board> searchByKeyword(@Param("keyword") String keyword, @Param("schoolId") Long schoolId);

}
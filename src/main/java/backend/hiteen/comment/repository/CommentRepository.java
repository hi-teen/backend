package backend.hiteen.comment.repository;

import backend.hiteen.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT MAX(c.anonymousNumber) FROM Comment c WHERE c.board.id = :boardId")
    Integer findMaxAnonymousNumberByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.parentComment IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findRootsByBoardId(@Param("boardId") Long boardId);
}

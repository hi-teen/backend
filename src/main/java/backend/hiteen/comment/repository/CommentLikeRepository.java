package backend.hiteen.comment.repository;

import backend.hiteen.comment.entity.Comment;
import backend.hiteen.comment.entity.CommentLike;
import backend.hiteen.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);

    int countByComment(Comment comment);

    void deleteByCommentAndMember(Comment comment, Member member);

}

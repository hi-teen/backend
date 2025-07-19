package backend.hiteen.comment.entity;

import backend.hiteen.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "comment_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "member_id"})
)
public class CommentLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}

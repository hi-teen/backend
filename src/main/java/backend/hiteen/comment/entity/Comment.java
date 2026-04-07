package backend.hiteen.comment.entity;

import backend.hiteen.board.entity.Board;
import backend.hiteen.global.entity.BaseTimeEntity;
import backend.hiteen.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    // 대댓글
    @Builder.Default
    @BatchSize(size=100)
    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> childrenComment = new ArrayList<>();

    private String content;

    private Integer anonymousNumber;

}

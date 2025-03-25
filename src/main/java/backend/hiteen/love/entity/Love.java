package backend.hiteen.love.entity;

import backend.hiteen.board.entity.Board;
import backend.hiteen.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;
import backend.hiteen.member.entity.Member;
import org.springframework.context.annotation.Bean;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Love {
    @Id
    @Column(name="love_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public Love(Member member, Board board){
        this.member=member;
        this.board=board;
    }

}

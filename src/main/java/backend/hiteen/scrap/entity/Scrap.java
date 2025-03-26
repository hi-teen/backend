package backend.hiteen.scrap.entity;

import backend.hiteen.board.entity.Board;
import backend.hiteen.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Scrap {

    @Id
    @Column(name="scrap_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne
    @JoinColumn(name="board_id")
    Board board;

    private Scrap(Member member, Board board){
        this.member=member;
        this.board=board;
    }

    public static Scrap create(Member member, Board board){
        return new Scrap(member,board);
    }

}

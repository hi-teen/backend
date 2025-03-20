package backend.hiteen.member.entity;

import backend.hiteen.board.entity.Board;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
    @OneToMany(mappedBy = "member")
    private List<Board> boards;
}
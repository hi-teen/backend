package backend.hiteen.member.entity;

import backend.hiteen.board.entity.Board;
import backend.hiteen.love.entity.Love;
import backend.hiteen.scrap.entity.Scrap;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;


    @Embedded
    private Password password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private int gradeNumber;

    @Column(nullable = false)
    private int classNumber;

    @OneToMany(mappedBy = "member")
    private List<Board> boards;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Love> loves;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps;

    @Builder
    private Member(String email, String password, String name, String nickname, String school, int gradeNumber, int classNumber, PasswordEncoder encoder) {
        this.email = email;
        this.password = new Password(password, encoder);
        this.name = name;
        this.nickname = nickname;
        this.school = school;
        this.classNumber = classNumber;
        this.gradeNumber = gradeNumber;
    }


}
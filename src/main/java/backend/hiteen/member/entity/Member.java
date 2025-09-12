package backend.hiteen.member.entity;

import backend.hiteen.board.entity.Board;
import backend.hiteen.externalapi.school.entity.School;
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

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

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

    //친구추천
    @Column(unique = true, length = 20 /*, nullable = true 기본값*/)
    private String referralCode;

    @ManyToOne
    @JoinColumn(name = "referral_by_id")
    private Member referredBy;

    @OneToMany(mappedBy = "referredBy")
    private List<Member> referredMembers;


    @Builder
    private Member(String email, String password, String name,
                   School school, int gradeNumber, int classNumber, PasswordEncoder encoder) {
        this.email = email;
        this.password = new Password(password, encoder);
        this.name = name;
        this.school = school;
        this.classNumber = classNumber;
        this.gradeNumber = gradeNumber;
    }

    public void updateProfile(
            String name,
            String nickname,
            String email,
            String password,
            String passwordConfirm,
            int gradeNumber,
            int classNumber,
            PasswordEncoder encoder
    ) {
        if (email != null) this.email = email;
        if (password != null) this.password = new Password(password, encoder);

        this.name = name;
        this.gradeNumber = gradeNumber;
        this.classNumber = classNumber;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public void setReferredBy(Member referrer) {
        this.referredBy = referrer;
    }

}
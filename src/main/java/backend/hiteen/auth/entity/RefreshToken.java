package backend.hiteen.auth.entity;

import backend.hiteen.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false, unique = true)
    private Member member;

    @Column(nullable = false)
    private String token;

    @Builder
    private RefreshToken(Member member, String token){
        this.member=member;
        this.token=token;
    }

    public void updateToken(String newToken){
        this.token=newToken;
    }
}

package backend.hiteen.member.dto.request;

import backend.hiteen.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
//TODO: @Schema(description, example) 각 필드마다 추가,
public class MemberCreateRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 한번 더 입력해주세요.")
    private String passwordConfirm;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "학교를 입력해주세요.")
    private String school;

    private int gradeNumber;

    private int classNumber;

    public Member toEntity(PasswordEncoder encoder){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .school(school)
                .gradeNumber(gradeNumber)
                .classNumber(classNumber)
                .encoder(encoder)
                .build();
    }
}
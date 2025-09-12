package backend.hiteen.member.dto.request;

import backend.hiteen.externalapi.school.entity.School;
import backend.hiteen.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "학교를 입력해주세요.")
    private Long schoolId;

    private int gradeNumber;

    private int classNumber;

    @Size(max = 20, message = "추천 코드는 최대 20자입니다.")
    private String referralCode;

    public Member toEntity(School school, PasswordEncoder encoder){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .school(school)
                .gradeNumber(gradeNumber)
                .classNumber(classNumber)
                .encoder(encoder)
                .build();
    }
}
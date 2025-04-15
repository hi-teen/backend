package backend.hiteen.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Password {

    private static final Pattern PASSWORD_PATTERN=Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~`!@#$%^&*()_\\-+=]{6,}$");

    @Column(name="password", nullable = false)
    private String password;

    public Password(String password, PasswordEncoder encoder){
        validatePasswordPattern(password);
        this.password=encoder.encode(password);
    }

    public boolean matches(String password, PasswordEncoder encoder){
        return encoder.matches(password,this.password);
    }

    private void validatePasswordPattern(String password){
        if (password==null || !PASSWORD_PATTERN.matcher(password).matches()){
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
        }
    }

}

package backend.hiteen.auth.security;

import backend.hiteen.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
public class CustomUserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserPrincipal(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword().getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /** 인증 ID 로 사용할 필드 */
    @Override
    public String getUsername() {
        return email;
    }

    // 계정 상태는 필요에 따라 변경
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}

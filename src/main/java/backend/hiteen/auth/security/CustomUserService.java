package backend.hiteen.auth.security;

import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->
                                     new UsernameNotFoundException("존재하지 않는 이메일입니다. email=" + email)
                );
        return new CustomUserPrincipal(member);
    }
}

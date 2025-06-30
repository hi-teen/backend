package backend.hiteen.auth.service;

import backend.hiteen.auth.dto.response.TokenResponse;
import backend.hiteen.auth.entity.RefreshToken;
import backend.hiteen.auth.jwt.JwtTokenProvider;
import backend.hiteen.auth.repository.RefreshTokenRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!member.getPassword().isPasswordMatch(password, passwordEncoder)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        refreshTokenRepository.findByMember(member).ifPresentOrElse(
                existing -> {
                existing.updateToken(refreshToken);
                refreshTokenRepository.save(existing);
                },
                () -> {
                    RefreshToken newRefreshToken = RefreshToken.builder()
                            .member(member)
                            .token(refreshToken)
                            .build();
                    refreshTokenRepository.save(newRefreshToken);
                }
        );

        return new TokenResponse(accessToken,refreshToken);
    }

    public TokenResponse reissue(String refreshToken){
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        String email=jwtTokenProvider.getEmail(refreshToken);
        Member member=memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("회원을 찾을 수 없습니다."));

        RefreshToken saved=refreshTokenRepository.findByMember(member)
                .orElseThrow(()->new IllegalArgumentException("저장된 Refresh Token이 없습니다."));
        if (!saved.getToken().equals(refreshToken)){
            throw new IllegalArgumentException("Refresh Token이 일치하지 않습니다.");
        }
        String newAccessToken= jwtTokenProvider.createAccessToken(email);
        String newRefreshToken= jwtTokenProvider.createRefreshToken(email);

        saved.updateToken(newRefreshToken);
        refreshTokenRepository.save(saved);

        return new TokenResponse(newAccessToken,newRefreshToken);
    }
}

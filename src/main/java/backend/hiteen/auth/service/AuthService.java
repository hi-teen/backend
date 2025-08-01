package backend.hiteen.auth.service;

import backend.hiteen.auth.dto.response.TokenResponse;
import backend.hiteen.auth.entity.RefreshToken;
import backend.hiteen.auth.jwt.JwtTokenProvider;
import backend.hiteen.auth.repository.RefreshTokenRepository;
import backend.hiteen.member.entity.Member;
import backend.hiteen.member.exception.MemberNotFoundException;
import backend.hiteen.member.exception.MemberPasswordNotMatchException;
import backend.hiteen.member.exception.MemberRefreshTokenMismatchException;
import backend.hiteen.member.exception.MemberRefreshTokenNotFoundException;
import backend.hiteen.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (!member.getPassword().isPasswordMatch(password, passwordEncoder)) {
            throw new MemberPasswordNotMatchException();
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
            throw new MemberRefreshTokenMismatchException();
        }

        String email=jwtTokenProvider.getEmail(refreshToken);
        Member member=memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        RefreshToken saved=refreshTokenRepository.findByMember(member)
                .orElseThrow(MemberRefreshTokenNotFoundException::new);
        if (!saved.getToken().equals(refreshToken)){
            throw new MemberRefreshTokenMismatchException();
        }
        String newAccessToken= jwtTokenProvider.createAccessToken(email);
        String newRefreshToken= jwtTokenProvider.createRefreshToken(email);

        saved.updateToken(newRefreshToken);
        refreshTokenRepository.save(saved);

        return new TokenResponse(newAccessToken,newRefreshToken);
    }

    //로그아웃
    public void logout(String email){
        Member member=memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        refreshTokenRepository.findByMember(member).ifPresent(refreshTokenRepository::delete);
    }
}

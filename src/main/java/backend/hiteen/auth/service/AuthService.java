package backend.hiteen.auth.service;

import backend.hiteen.auth.dto.response.TokenResponse;
import backend.hiteen.auth.entity.RefreshToken;
import backend.hiteen.auth.jwt.JwtTokenProvider;
import backend.hiteen.auth.repository.RefreshTokenRepository;
import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;
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
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getPassword().isPasswordMatch(password, passwordEncoder)) {
            throw new BusinessException(ErrorCode.MEMBER_PASSWORD_NOT_MATCH);
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
            throw new BusinessException(ErrorCode.MEMBER_REFRESH_TOKEN_MISMATCH);
        }

        String email=jwtTokenProvider.getEmail(refreshToken);
        Member member=memberRepository.findByEmail(email)
                .orElseThrow(()->new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        RefreshToken saved=refreshTokenRepository.findByMember(member)
                .orElseThrow(()->new BusinessException(ErrorCode.MEMBER_REFRESH_TOKEN_NOT_FOUND));
        if (!saved.getToken().equals(refreshToken)){
            throw new BusinessException(ErrorCode.MEMBER_REFRESH_TOKEN_MISMATCH);
        }
        String newAccessToken= jwtTokenProvider.createAccessToken(email);
        String newRefreshToken= jwtTokenProvider.createRefreshToken(email);

        saved.updateToken(newRefreshToken);
        refreshTokenRepository.save(saved);

        return new TokenResponse(newAccessToken,newRefreshToken);
    }
}

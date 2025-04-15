package backend.hiteen.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        String token=resolveToken(request);
        System.out.println("🔍 Authorization 헤더에서 추출한 토큰: " + token);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            System.out.println("✅ 토큰 유효성 검사 통과");

            String email=jwtTokenProvider.getEmail(token);
            System.out.println("📧 토큰에서 추출한 이메일: " + email);

            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(email,null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("🔐 SecurityContext에 인증 정보 등록 완료");
        } else {
            System.out.println("토큰이 없거나 유효하지 않음");
        }

        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearer=request.getHeader("Authorization");
        System.out.println("🧾 Authorization 원문 헤더 값: " + bearer);

        if (StringUtils.hasText(bearer)&&bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }
}

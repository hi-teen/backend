package backend.hiteen.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private  String secretKey;

    private final long accessTokenvalidity=1000L * 60 * 60; //토큰 유효시간:1시간
    private final long refreshTokenvalidity=1000L*60*60*24*7; //토큰 유효시간 7일

    @PostConstruct
    protected void init(){
        this.secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String email){
        return createToken(email,accessTokenvalidity);
    }

    public String createRefreshToken(String email){
        return createToken(email, refreshTokenvalidity);
    }

    public String createToken(String email, long validity){
        Claims claims= Jwts.claims().setSubject(email);
        Date now=new Date();
        Date expiry=new Date(now.getTime()+validity); //1시간 후 만료

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getEmail(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

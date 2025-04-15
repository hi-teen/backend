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

    private final long validity=1000L * 60 * 60; //토큰 유효시간:1시간

    @PostConstruct
    protected void init(){
        this.secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email){
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
}

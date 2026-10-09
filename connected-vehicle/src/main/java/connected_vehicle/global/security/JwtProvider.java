package connected_vehicle.global.security;

import connected_vehicle.domain.user.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(User user){

        SecretKey key = getSigningKey();

        Date now = new Date();

        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserId(String token){
        return Long.parseLong(Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }
}

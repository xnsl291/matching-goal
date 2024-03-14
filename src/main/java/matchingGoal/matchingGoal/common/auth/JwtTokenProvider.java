package matchingGoal.matchingGoal.common.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.member.exception.InvalidTokenException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisService redisService;
    private static final String BEARER_TYPE = "Bearer ";
    private static final String BLACK_TOKEN_PREFIX = "BLACK: ";
    private static final Long accessTokenExpirationTimeInSeconds = 30 * 60 * 1000L;
    private static final Long refreshTokenExpirationTimeInSeconds = 7 * 24 * 60 * 60 * 1000L;
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private String getRefreshTokenKey(String email) {
        return "RT_" + email;
    }

    public JwtToken generateToken(Long id, String email ){
        long now = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id.toString());
        claims.put("email", email);

        String accessToken = Jwts.builder()
                .setSubject(email)
                .setClaims(claims)
                .setExpiration(new Date(now + accessTokenExpirationTimeInSeconds))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setClaims(claims)
                .setExpiration(new Date(now + refreshTokenExpirationTimeInSeconds))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        saveRefreshToken(email, refreshToken);  //redis key : RT_{email}
        return new JwtToken(accessToken,refreshToken);
    }

    public void saveRefreshToken(String email, String token){
        redisService.setData(getRefreshTokenKey(email), token, refreshTokenExpirationTimeInSeconds);
    }

    public void deleteToken(String email) {
        String rtKey = getRefreshTokenKey(email);
        if (redisService.getData(rtKey) == null)
            throw new InvalidTokenException();

        redisService.deleteData(rtKey);
    }

    public void setBlacklist(String token) {
        redisService.setData(BLACK_TOKEN_PREFIX + getEmail(token), token, refreshTokenExpirationTimeInSeconds);
    }

    public boolean validateToken(String token) {
        try {
            if(redisService.getData(BLACK_TOKEN_PREFIX + getEmail(token)) != null )
                return false;

            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(getRawToken(token));
            return true;
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public String getRawToken(String token) {
        if (token != null && token.startsWith(BEARER_TYPE)) {
            return token.substring(7);
        }
        return token;
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(getRawToken(token)).getBody()
                .get("email")
                .toString();
    }

    public  Long getId(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(getRawToken(token))
                .getBody()
                .get("id", Long.class);
    }
}

package matchingGoal.matchingGoal.common.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.member.exception.ExpiredTokenException;
import matchingGoal.matchingGoal.member.exception.InvalidTokenException;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisService redisService;
    private static final String BLACK_TOKEN_PREFIX = "BLACK: ";
    private static final Long accessTokenExpirationTimeInSeconds = 30 * 60 * 1000L;
    private static final Long refreshTokenExpirationTimeInSeconds = 7 * 24 * 60 * 60 * 1000L;

    private final String key = "236979CB6F1AD6B6A6184A31E6BE37DB3818CC36871E26235DD67DCFE4041492";

    public JwtToken generateToken(Long id, String email, String nickname) {
        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
                .setId(id.toString())
                .setSubject(email)
                .setAudience(nickname)
                .setExpiration(new Date(now + accessTokenExpirationTimeInSeconds))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        String refreshToken = Jwts.builder()
                .setId(id.toString())
                .setSubject(email)
                .setAudience(nickname)
                .setExpiration(new Date(now + refreshTokenExpirationTimeInSeconds))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        saveRefreshToken(email, refreshToken);  //redis key : RT_{email}

        return new JwtToken(accessToken,refreshToken);
    }


    public void validateToken(String token) {
        try {
            // 로그아웃한 토큰일 경우
            if (redisService.getData(BLACK_TOKEN_PREFIX + getEmail(token)) != null)
                throw new ExpiredTokenException();

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
    }

    public String getEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(key)
                    .parseClaimsJws(token).getBody()
                    .getSubject()
                    ;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Long getId(String token) {
        try{
            return Long.valueOf(parseClaims(token)
                    .getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    private String getRefreshTokenKey(String email) {
        return "RT_" + email;
    }

    public void saveRefreshToken(String email, String token) {
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
}

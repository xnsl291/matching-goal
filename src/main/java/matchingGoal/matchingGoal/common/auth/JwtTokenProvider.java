package matchingGoal.matchingGoal.common.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private static final String BLACK_TOKEN_PREFIX = "BLACK: ";
    private static final Long accessTokenExpirationTimeInSeconds =  30 * 60 * 1000L;
    private static final Long refreshTokenExpirationTimeInSeconds = 7 * 24 * 60 * 60 * 1000L;

    @Value("${jwt.token.secret}")
    private String key;

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
        token = getRawToken(token);

        try {
            // 로그아웃한 토큰일 경우
            if (redisService.hasKeyAndValue( BLACK_TOKEN_PREFIX + getEmail(token) , token))
                throw new CustomException(ErrorCode.EXPIRED_TOKEN);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
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
        token = getRawToken(token);

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
        redisService.setData(getRefreshTokenKey(email), getRawToken(token), refreshTokenExpirationTimeInSeconds);
    }

    public void deleteToken(String email) {
        String rtKey = getRefreshTokenKey(email);
        if (redisService.getData(rtKey) == null)
            throw new CustomException(ErrorCode.INVALID_TOKEN);

        redisService.deleteData(rtKey);
    }

    public void setBlacklist(String token) {
        token = getRawToken(token);

        redisService.setBlackList(BLACK_TOKEN_PREFIX + getEmail(token), token, refreshTokenExpirationTimeInSeconds);
    }

    /**
     * 토큰 갱신
     */
    public JwtToken refresh(String token){
        token = getRawToken(token);

        // 토큰 검증
        validateToken(token);
        Member member = memberRepository.findByEmail(getEmail(token)).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (! redisService.hasKey(getRefreshTokenKey(member.getEmail()) ))  // redis에 키가 저장되어 있지 않으면
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);

        return generateToken(member.getId(),member.getEmail(),member.getNickname());
    }

    private String getRawToken(String token) {
        return token.replace("Bearer ","");
    }
}

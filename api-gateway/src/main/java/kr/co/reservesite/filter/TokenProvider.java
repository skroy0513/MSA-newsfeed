package kr.co.reservesite.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {

    private final Key key;
    private final RedisTemplate redisTemplate;
    private final long tokenValidTime = 30 * 60 * 1000L;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, RedisTemplate redisTemplate) {
        byte [] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    public String createAccessToken(String refreshToken) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        String email = claims.getSubject();
        Long userId =claims.get("id", Long.class);
        String authorities = claims.get("auth", String.class);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + tokenValidTime);

        String accessToken = Jwts.builder()
                .setSubject(email)  // 유저의 email
                .claim("auth", authorities)
                .claim("id", userId)
                .setIssuedAt(now)// 유저의 권한
                .setExpiration(expireDate)  // 만료시간 (30분)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return accessToken;
    }

    /**
     * jwt 유효성 검사
     * @param accessToken
     * @return boolean
     */
    public boolean validateToken(String accessToken) {
        log.info("검사시작");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (NullPointerException e) {
            log.info("인증토큰이 없는 요청이 들어왔습니다.");
        } catch (Exception e) {
            log.error("기타 인증 정보가 잘못됐습니다. {}", e.getMessage());
        }
        return false;
    }

    public Long getUserId(String accessToken) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        Long userId = Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody().get("id", Long.class);
        return userId;
    }

    /**
     * accessToken으로 refreshToken이 저장되어있는지 확인
     * @param accessToken
     * @return
     */
    public boolean isExistRefreshToken(String accessToken) {

        String result = (String) redisTemplate.opsForValue().get(accessToken);

        if (StringUtils.hasText(result)) {
            return true;
        }
        return false;
    }

    /**
     * accessToken 헤더에 설정
     * @param response
     * @param accessToken
     */
    public void setHeaderAccessToken(ServerHttpResponse response, String accessToken) {
        response.getHeaders().set("Authorization", "Bearer "+ accessToken);
    }

    /**
     * 현재 만료시간 확인
     * @param accessToken
     * @return
     */
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken).getBody().getExpiration();

        long now = new Date().getTime();

        return expiration.getTime() - now;
    }

}

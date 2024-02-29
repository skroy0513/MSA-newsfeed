package kr.co.reservesite.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    public static class Config {}

    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

    public AuthFilter(TokenProvider tokenProvider, RedisTemplate redisTemplate) {
        super(Config.class);
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {

                String accessToken = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).substring(7);
                validBlackToken(accessToken);

                if (tokenProvider.validateToken(accessToken)) {
                    Long userId = tokenProvider.getUserId(accessToken);
                    log.info("유저의 아이디는 -> {}", userId);

                    request.mutate().header("Auth", "true").build();
                    request.mutate().header("userId", String.valueOf(userId)).build();
                    tokenProvider.setHeaderAccessToken(response, accessToken);

                    return chain.filter(exchange);
                } else if(!tokenProvider.validateToken(accessToken)) {
                    boolean isExist = tokenProvider.isExistRefreshToken(accessToken);

                    if (isExist) {
                        String refreshToken = (String) redisTemplate.opsForValue().get(accessToken);

                        tokenProvider.validateToken(refreshToken);
                        String newAccessToken = tokenProvider.createAccessToken(refreshToken);
                        log.info("newAccessToken -> {}", newAccessToken);

                        Long oldExpiration = redisTemplate.getExpire(accessToken);

                        redisTemplate.opsForValue().set(newAccessToken, refreshToken, oldExpiration*1000, TimeUnit.MILLISECONDS);

                        redisTemplate.delete(accessToken);

                        Long userId = tokenProvider.getUserId(newAccessToken);

                        request.mutate().header("Auth", "true").build();
                        request.mutate().header("userId", String.valueOf(userId)).build();

                        tokenProvider.setHeaderAccessToken(response, newAccessToken);

                        return chain.filter(exchange);
                    }
                }
            }
            return onError(exchange, "권한없음 : 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }

    private void validBlackToken(String accessToken) {
        //Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
        String logout = (String) redisTemplate.opsForValue().get(accessToken);
        if(logout != null && logout.equals("logout")) {
            throw new RuntimeException("로그아웃 처리된 엑세스 토큰입니다.");
        }
    }

}

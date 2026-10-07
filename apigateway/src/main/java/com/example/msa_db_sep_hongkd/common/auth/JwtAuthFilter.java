package com.example.msa_db_sep_hongkd.common.auth;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter {
    @Value("${jwt.secretKey}")
    private String secretKey;
    private static final List<String> ALLOWED_PATH =List.of(
            "/member/create",
            "/member/doLogin",
            "/member/refresh-token",
            "/product/list",
            "/product/bestSeller"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("token 검증 시작");
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String path = exchange.getRequest().getURI().getRawPath();
        System.out.println(path);
        if(ALLOWED_PATH.contains(path)){
            return chain.filter(exchange);
        }
        try{
            if (bearerToken ==null || !bearerToken.startsWith("Bearer")){
                throw new IllegalArgumentException("유효하지 않은 토큰입니다");
            }
            String token = bearerToken.substring(7);
            Claims claims =Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String userId=claims.getSubject();
            String role =claims.get("role",String.class);
            String status = claims.get("status",String.class);

            ServerWebExchange modifiedExchange =exchange.mutate()
                    .request(builder -> builder
                            .header("X-USER-ID",userId)
                            .header("X-USER-ROLE",role)
                            .header("X-USER-STATUS",status))
                    .build();
            return chain.filter(modifiedExchange);

        }catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException | SignatureException | UnsupportedJwtException e){
            e.printStackTrace();
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return  exchange.getResponse().setComplete();
        }
    }
}


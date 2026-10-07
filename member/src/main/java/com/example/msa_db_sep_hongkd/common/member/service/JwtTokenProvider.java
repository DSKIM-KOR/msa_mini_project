package com.example.msa_db_sep_hongkd.common.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.expiration}")//yaml파일에서 설정된 jwt : expiration을 그대로 가져와서 사용.
    private int expiration;

    @Value("${jwt.expirationRt}")//yaml파일에서 설정된 jwt : expiration을 그대로 가져와서 사용.
    private int expirationRt;

    @Value("${jwt.secretKey}")//yaml파일에서 설정된 jwt : secretKey 그대로 가져와서 사용
    private String secretKey;

    @Value("${jwt.secretKeyRt}")//yaml파일에서 설정된 jwt : secretKeyRt 그대로 가져와서 사용
    private String secretKeyRt;

    private Key ENCRYPT_SECRET_KEY;
    private Key ENCRYPT_RT_SECRET_KEY;

    @PostConstruct
    public void init(){
        ENCRYPT_SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS512.getJcaName());
        ENCRYPT_RT_SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKeyRt),
                SignatureAlgorithm.HS512.getJcaName());

    }

    //accessToken
    public String createToken(String id,String role,String status){
        Claims claims= Jwts.claims().setSubject(id);
        claims.put("role",role);
        claims.put("status",status);
        Date now = new Date();
        //claims는 사용자 정보(페이로드 정보)
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() +expiration*60*1000L))
                .signWith(ENCRYPT_SECRET_KEY)
                .compact();
        return token;
    }

    //refreshToken
    public String createRefreshToken(String id,String role){
        Claims claims= Jwts.claims().setSubject(id);
        claims.put("role",role);
        Date now = new Date();
        //claims는 사용자 정보(페이로드 정보)
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() +expirationRt*60*1000L))
                .signWith(ENCRYPT_RT_SECRET_KEY)
                .compact();
        return token;
    }
}

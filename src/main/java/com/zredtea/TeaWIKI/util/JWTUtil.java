package com.zredtea.TeaWIKI.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Getter
    @Value("${jwt.expiration:86400}")
    private Long expiration;

    private final RedisTemplate<String, Object> redisTemplate;

    public JWTUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if(keyBytes.length < 32) {
            SecretKey secretKey = Jwts.SIG.HS256.key().build();
            this.secretKey = secretKey.toString();
            return secretKey;
        }
        else {
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String generateToken(Integer userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        String token = Jwts.builder()
                .subject(userId.toString())
                .claim("username",username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();

        String redisKey = "USER_TOKEN" + userId;
        redisTemplate.opsForValue().set(redisKey, token, expiration, TimeUnit.SECONDS);

        return token;
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Integer.parseInt(claims.getSubject());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("username", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            String redisKey = "USER_TOKEN" + getUserIdFromToken(token);
            String redisToken = (String) redisTemplate.opsForValue().get(redisKey);

            return token.equals(redisToken);
        } catch (Exception e){
            log.debug(e.getMessage());
            return false;
        }
    }

    public void invalidateToken(Integer userId) {
        try {
            String redisKey = "USER_TOKEN" + userId;
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
}

package com.pos.commerce.infrastructure.user.Jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    // 최소 256비트(32바이트) 이상인 안전한 비밀키
    private final String secretKey = "my-very-secure-secret-key-of-at-least-32-bytes!";
    private final long validityInMilliseconds = 3600000; // 1시간

    private final Key key;

    public JwtProvider() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 토큰 생성
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)                  // 토큰 발급 시각
                .setExpiration(expiryDate)        // 만료 시각
                .signWith(key, SignatureAlgorithm.HS256) // 안전하게 HS256 서명
                .compact();
    }

    /**
     * 토큰에서 username 추출
     */
    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.", e);
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.", e);
        }
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

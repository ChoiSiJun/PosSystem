package com.pos.commerce.util;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


public class JwtProvider {

    private final String secretKey = "my-very-secure-secret-key-of-at-least-32-bytes!";
    private final long validityInMilliseconds = 3600000; // 1시간

    private final Key key;

    public JwtProvider() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


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

package com.duckisoft.gardening.util;

import com.duckisoft.gardening.exception.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    public static final long JWT_TOKEN_VALIDITY = 2592000000L; //30 days

    @Value("${jwt.secret}")
    private String secret;

    //check if the token has expired
    private Boolean isTokenExpired(Date date) {
        return date.before(new Date());
    }

    //generate token for user
    public String generateToken(String username) {
        try {
            Map<String, Object> claims = new HashMap<>();
            return doGenerateToken(claims, username);
        } catch (Exception e) {
            throw new JwtException("Error generating JWT Token");
        }

    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey())
                .compact();
    }

    //validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        Jws<Claims> parsedToken = parseToken(token);
        final String username = parsedToken.getPayload().getSubject();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(parsedToken.getPayload().getExpiration()));
    }
    private Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
    }
    public String getUsernameFromToken(String token) {
        return parseToken(token).getPayload().getSubject();
    }
}

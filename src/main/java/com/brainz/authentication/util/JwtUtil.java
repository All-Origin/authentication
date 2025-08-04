package com.brainz.authentication.util;

import com.brainz.authentication.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret:4f6G8z!s9yA3eL1pXpV2cNqTk9mBxZ3e}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 days in milliseconds
    private Long refreshExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Long userId, String username, List<Roles> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", username);
        claims.put("roles", roles.stream().map(Enum::name).toList()); // store as List<String>
        return createToken(claims, userId.toString(), expiration);
    }

    public String generateRefreshToken(Long userId, String username, List<Roles> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", username);
        claims.put("roles", roles.stream().map(Enum::name).toList()); // store as List<String>
        return createToken(claims, userId.toString(), refreshExpiration);
    }

    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, String userId) {
        final String tokenUserId = extractUserId(token).toString();
        return (userId.equals(tokenUserId) && !isTokenExpired(token));
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //  Fixed version using JwtParser
    private Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build();
        return parser.parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("userName", String.class));
    }

    public List<Roles> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObject = claims.get("roles");

        if (rolesObject instanceof List<?> list) {
            return list.stream()
                    .map(Object::toString) // convert anything to string (safe fallback)
                    .map(roleStr -> {
                        try {
                            return Roles.valueOf(roleStr);
                        } catch (IllegalArgumentException e) {
                            return null; // or throw
                        }
                    })
                    .filter(role -> role != null)
                    .toList();
        }

        // If a single role is accidentally stored as a plain string
        if (rolesObject instanceof String roleStr) {
            try {
                return List.of(Roles.valueOf(roleStr));
            } catch (IllegalArgumentException e) {
                return List.of();
            }
        }

        return List.of(); // fallback
    }



    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

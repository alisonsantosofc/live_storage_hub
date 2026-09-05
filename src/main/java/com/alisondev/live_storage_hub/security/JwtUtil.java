package com.alisondev.live_storage_hub.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
  private final Key key;
  private final long jwtExpirationMs;

  public JwtUtil(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration}") long jwtExpirationMs) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.jwtExpirationMs = jwtExpirationMs;
  }

  public String generateToken(Long userId, String email, Long appId) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .addClaims(Map.of("userId", userId, "appId", appId, "email", email))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getEmailFromToken(String token) {
    return parseClaims(token).get("email", String.class);
  }

  public Long getUserIdFromToken(String token) {
    Object userId = parseClaims(token).get("userId");
    return userId != null ? Long.valueOf(userId.toString()) : null;
  }

  public Long getAppIdFromToken(String token) {
    Object appId = parseClaims(token).get("appId");
    return appId != null ? Long.valueOf(appId.toString()) : null;
  }

  public boolean validateToken(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

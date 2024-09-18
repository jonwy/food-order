package com.padepokan79.foodorder.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.padepokan79.foodorder.security.service.UserDetailsImplement;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class JwtUtils {
    
    @Value("${jwt.secret.key}")
    private String jwtSecret;
    @Value("${jwt.expirationms}")
    private Long jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

      UserDetailsImplement userPrincipal = (UserDetailsImplement) authentication.getPrincipal();

      Map<String, UserDetailsImplement> claims = new HashMap<>();
      claims.put("userDetails", userPrincipal);
      return Jwts.builder()
          .subject(userPrincipal.getUsername())
          .claims(claims)
          .issuedAt(new Date())
          .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
              .signWith(key())
          .compact();
    }
  
  private SecretKey key() {
    byte[] decoded = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(decoded);
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public Integer getIdFromJwtToken(String token) {
    UserDetailsImplement userDetails = (UserDetailsImplement)Jwts.parser().decryptWith(key()).build().parseSignedClaims(token).getPayload().get("userDetails");
    return userDetails.getId();
  }

  public boolean validateJwtToken(String authToken) {
    try {
        Jwts.parser().verifyWith(key()).build().parse(authToken);
        
        return true;
    } catch (MalformedJwtException e) {
        log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
        log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
        log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
        log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}

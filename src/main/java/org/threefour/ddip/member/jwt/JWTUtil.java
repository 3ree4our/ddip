package org.threefour.ddip.member.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {
  private Key secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
    this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  }

  public Long getId(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("id", Long.class);
  }

  public String getCategory(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("category", String.class);
  }

  public String getUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("username", String.class);
  }

  public String getNickname(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("nickname", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
  }

  public List<String> getRole(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("roles", List.class);
  }

  public String createJwt(Long id, List<String> role, String category, String username, String nickname ,Long expiredMs) {
    String jwt = Jwts.builder()
            .claim("id", id)
            .claim("roles", role)
            .claim("category", category)
            .claim("username", username)
            .claim("nickname", nickname)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey, SignatureAlgorithm.HS384)
            .compact();

    System.out.println("Generated JWT: " + jwt);

    return jwt;
  }
}
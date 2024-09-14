package org.threefour.ddip.member.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {
  private SecretKey secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
    byte[] bytes = Decoders.BASE64.decode(secret);
    secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }

  public Long getId(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("id", Long.class);
  }

  public String getUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("username", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
  }

  public String createJwt(Long id, String username, Long expiredMs) {
    return Jwts.builder()
            .claim("id", id)
            .claim("username", username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
  }
}
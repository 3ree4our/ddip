package org.threefour.ddip.member.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {
  private SecretKey secretKey;

  //value : application.properties에 있는 Secret key 가져옴
  public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
    byte[] bytes = Decoders.BASE64.decode(secret);
    secretKey = Keys.hmacShaKeyFor(bytes);
  }

  public String getUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("username", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
  }

  public String createJwt(String username, Long expiredMs) {
    return Jwts.builder()
            /* .setSubject(username) // 사용자 식별자값(ID) */
            .claim("username", username)//payload에 들어갈 key-value 쌍
            //TODO 권한도 같이 담아주기
            /* .claim() 추가 */
            .setIssuedAt(new Date(System.currentTimeMillis())) //발급일
            .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) //만료시간
            .signWith(secretKey) //암호화 알고리즘
            .compact();
  }
}
package com.gulaev.SnapSound.security;

import com.gulaev.SnapSound.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenProvider {
  public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

  public String generateToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Date now = new Date(System.currentTimeMillis());
    Date expiryDate = new Date(now.getTime() + SecurityConstant.EXPIRATION_TIME);

    String userId = Long.toString(user.getId());

    Map<String, Object> claimsMap = new HashMap<>();
    claimsMap.put("id", userId);
    claimsMap.put("username", user.getUsername());
    claimsMap.put("firstname", user.getName());
    claimsMap.put("lastname", user.getLastName());

    return Jwts.builder()
        .setSubject(userId)
        .addClaims(claimsMap)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, SecurityConstant.SECRET)
        .compact();

  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .setSigningKey(SecurityConstant.SECRET)
          .parseClaimsJws(token);
      return true;
    }catch (SignatureException |
            MalformedJwtException |
            ExpiredJwtException |
            UnsupportedJwtException |
            IllegalArgumentException ex) {
      LOG.error(ex.getMessage());
      return false;
    }
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(SecurityConstant.SECRET)
        .parseClaimsJws(token)
        .getBody();
    String id = (String) claims.get("id");
    return Long.parseLong(id);
  }

}

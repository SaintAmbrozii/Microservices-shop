package com.example.gatawayservice.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

   // @Value("${jwt.secret}")
    private final String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

   private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }




    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        return this.validateJwtToken(token) || this.isTokenExpired(token);
    }
    private boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return false;
        }  catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}" + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}"+ e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}"+ e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}"+ e.getMessage());
        } catch (Exception e) {
            System.out.println("-------------------------");
        }

        return true;
    }

  //  public String getRole(String token) {
 //       return this.getAllClaimsFromToken(token).get("role").toString();
 //   }

 //   public void validateToken(final String token) {
  //      Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
  //  }



  //  private Key getSignKey() {
  //      byte[] keyBytes = Decoders.BASE64.decode(secret);
  //      return Keys.hmacShaKeyFor(keyBytes);
  //  }
}

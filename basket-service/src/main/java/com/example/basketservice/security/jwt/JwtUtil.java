package com.example.basketservice.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtUtil {

   // @Value("${jwt.secret}")
    private final String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

   private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getClaimFromToken(token,Claims::getId));
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
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

package com.example.userservice.security.jwt;



import com.example.userservice.config.AppProperties;
import com.example.userservice.domain.User;
import com.example.userservice.security.UserPrincipal;
import com.example.userservice.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

import static com.example.userservice.util.CotstantUtil.AUTHORIZATION_SECURITY_HEADER;
import static com.example.userservice.util.CotstantUtil.BEARER;
import static java.util.stream.Collectors.joining;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final AppProperties appProperties;
    private final CustomUserDetailsService customUserDetailsService;


    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(appProperties.getAuth().getTokenSecret().getBytes());
    }

  //  @PostConstruct
  //  public void init() {
 //       String secret = Base64.getEncoder().encodeToString(this.appProperties.getAuth().getTokenSecret().getBytes());
  //      this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  //  }


    public String createToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();

    }

    public String createRefreshToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getRefreshExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();

    }



    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserById(getUserIdFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token);
            //  parseClaimsJws will check expiration date. No need do here.
            log.info("expiration date: {}", claims.getBody().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }




}

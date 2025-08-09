package org.example.languagemaster.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.entity.Users;
import io.jsonwebtoken.security.Keys;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {
  private final UserDetailsService userDetailsService;
  private final UserRepository userRepository;

  public String generateToken(String email) {
    Users user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("error_in_fetching_user_data"));
    if (user.isVerified())
      return Jwts.builder()
          .setSubject(user.getEmail())
          .setIssuedAt(new Date())
          .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
          .claim("fullname", user.getFirstname() + " " + user.getLastname())
          .claim("userId", user.getId())
          .claim("role", user.getRole().toString())
          .signWith(getKey())
          .compact();
    else
      return "not_verified";
  }

  public boolean isValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public Key getKey() {
    byte[] bytes =
        Decoders.BASE64.decode("1234567891234567891234567891234567891234567891234567891234567890");
    return Keys.hmacShaKeyFor(bytes);
  }

  public Authentication getUser(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();

    String roles = claims.get("role", String.class);
    List<SimpleGrantedAuthority> authorities =
        roles != null
            ? Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
            : List.of();

    UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

    return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
  }

  public String getUsernameFromToken(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }
}

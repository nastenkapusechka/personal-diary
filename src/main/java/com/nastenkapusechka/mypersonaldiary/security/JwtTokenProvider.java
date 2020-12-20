package com.nastenkapusechka.mypersonaldiary.security;

import com.nastenkapusechka.mypersonaldiary.entities.Role;
import com.nastenkapusechka.mypersonaldiary.service.UserDetailsFactory;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserDetailsImpl;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secretKey}")
    private String secretKey;
    @Value("${jwt.token.expiration}")
    private long validityInMilliseconds;

    private final UserServiceImpl userService;

    @Autowired
    public JwtTokenProvider(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<Role> roleList) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roleList.stream()
                            .map(Role::getName)
                            .collect(Collectors.toList()));

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(expiration)
                .setIssuedAt(now)
                .setClaims(claims)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetailsImpl user =
                UserDetailsFactory.getUser(userService.findByUsername(username));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public boolean validateToken(String token) {

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT expired or invalid");
        }

        return true;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

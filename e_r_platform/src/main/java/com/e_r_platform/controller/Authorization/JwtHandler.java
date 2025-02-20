package com.e_r_platform.controller.Authorization;

import com.e_r_platform.service.impl.CustomUserDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtHandler {
    @Autowired
    private CustomUserDetailService userDetailService;

    private static final Logger log = LoggerFactory.getLogger(JwtHandler.class);

    private final Key key; // 用于签名的密钥

    public JwtHandler() {
        // 在构造函数中生成密钥
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24); // Expire in 24 hours

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key) // 使用自动生成的密钥签名
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().getSubject();
    }

    public UserDetails getUserDetails(String token) {
        return userDetailService.loadUserByUsername(getUsernameFromToken(token));
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }


    public boolean checkUser(Authentication authentication, String email) {
        String currentUser = authentication.getName();
        return currentUser.equals(email);
    }
}
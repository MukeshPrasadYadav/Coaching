package com.projects.coaching_offline_support.auth.Services;

import com.projects.coaching_offline_support.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.security}")
    private  String secretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 *60 * 10))
                .signWith(getSecretKey())
                .compact();

    }

    public String generateRefreshToken(User user){

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 *60*60*24*365 ))
                .signWith(getSecretKey())
                .compact();

    }

    public UUID getUserIdFromToken(String token){
        Claims claims = Jwts.parser().build().parseSignedClaims(token).getPayload()  ;
        return UUID.fromString( claims.getSubject());
    }


}

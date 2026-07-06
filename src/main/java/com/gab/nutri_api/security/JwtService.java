package com.gab.nutri_api.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.model.Utilisateur;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
  
    public String generateToken(Utilisateur utilisateur) {

        return Jwts.builder()
            .subject(utilisateur.getEmail())
            // pas obligatoire, rôle sera récupéré autrement .claim("role", utilisateur.getRole().name())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(getSigningKey())
            .compact();
    }
    
    public String extractUsername(String token) {

        Claims claims = Jwts.parser()
        	    .verifyWith(getSigningKey())
        	    .build()
        	    .parseSignedClaims(token)
        	    .getPayload();

        return claims.getSubject();
     }
    
    private boolean isTokenExpired(String token) {
    	
    	Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    	
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    
}

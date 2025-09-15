package com.codewithdurgesh.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtTokenHelper {
	
	 private final String SECRET_KEY = "1234567890_abcdefghij_1234567890abcd";

	    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

	    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); 

	    public String generateToken(UserDetails userDetails) {
	        return Jwts.builder()
	                .subject(userDetails.getUsername())
	                .issuedAt(new Date())
	                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(key)
	                .compact();
	    }

	    public String extractUsername(String token) {
	        return Jwts.parser()
	                .verifyWith((SecretKey) key)
	                .build()
	                .parseSignedClaims(token)
	                .getPayload()
	                .getSubject();
	    }

	    public boolean validateToken(String token) {
	        try {
	            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
	            return true;
	        } catch (JwtException | IllegalArgumentException e) {
	            return false;
	        }
	    }
}

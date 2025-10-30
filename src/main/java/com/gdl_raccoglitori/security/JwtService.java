package com.gdl_raccoglitori.security;

import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import com.gdl_raccoglitori.model.*;

@Service
public class JwtService
{
	@Value("${jwt.secret}")
	private String secretKey;

    private final long ACCESS_TOKEN_EXPIRATION = 3600000;
    private final long REFRESH_TOKEN_EXPIRATION = 604800000;

    public String generateToken(Utente utente)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ruolo", utente.getRuolo().name());
        claims.put("id", utente.getId());
        claims.put("email", utente.getEmail());
        return createToken(claims, utente.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(Utente utente)
    {
        return createToken(new HashMap<>(), utente.getUsername(), REFRESH_TOKEN_EXPIRATION);
    }
    
    public String generateResetPasswordToken(Utente utente)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "reset");
        return createToken(claims, utente.getUsername(), 600000);
    }
    
    private String createToken(Map<String, Object> claims, String subject, long expirationTime)
    {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException, SignatureException, MalformedJwtException, UnsupportedJwtException, IllegalArgumentException
    {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            // Log l'errore per debug, ma restituisci null/gestisci l'errore
            return null; 
        }
    }

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token)
    {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public LocalDateTime extractExpirationAsLocalDateTime(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate != null ? expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

    public String extractRole(String token)
    {
        return extractClaim(token, claims -> claims.get("ruolo", String.class));
    }
}
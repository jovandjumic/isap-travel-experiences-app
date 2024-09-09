package com.jovandjumic.isap_travel_experiences_app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // Ekstraktovanje korisničkog imena iz JWT tokena
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Ekstraktovanje bilo koje vrednosti (claim) iz JWT tokena
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }


    // Proverava da li je JWT token istekao
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Ekstraktovanje datuma isteka JWT tokena
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Ekstraktovanje svih podataka (claims) iz JWT tokena
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Kreiranje ključa za potpisivanje (iz tajnog ključa definisanog u konfiguraciji)
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

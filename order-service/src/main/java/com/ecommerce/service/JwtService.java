package com.ecommerce.service;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String SECRET_KEY="mySecretKeywhdguhbfwiubijasnbcijasbfuhwbkasmklckbasncbajnnvksdn";

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

package br.com.indra.derek_lisboa.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private String SECRET_TOKEN = "u8G5vZ1Qw3XrL6mS9yT2pK4nJ7dF0bH3L8qV5eR1xA6sN0zC";

    private long EXPIRATION_TIME = 60 * 60 * 1000;

    /**
     * Transformar o SECRET_TOKEN de String para bytes
     *e entao pega o array de bytes e retorna um objeto do tipo key para o signWith
     * e esse key sera usado para assinar o token com HS256
     *
     * OBS.: signWith na aceita mais String direto, mas esta depreciado
     * usar bytes da mais segurança e compatibilidade com o HMAC SHA-256.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_TOKEN.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}

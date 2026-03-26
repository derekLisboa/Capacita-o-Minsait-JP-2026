package br.com.indra.derek_lisboa.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private static final String SECRET_TOKEN = "u8G5vZ1Qw3XrL6mS9yT2pK4nJ7dF0bH3L8qV5eR1xA6sN0zC";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    /**
     * Converte o SECRET_TOKEN (String) para um objeto Key.
     *
     * O metodo getBytes() transforma a String em um array de bytes,
     * que é então utilizado pelo Keys.hmacShaKeyFor para gerar a chave de assinatura.
     *
     * Essa chave é usada no signWith para assinar o token com o algoritmo HS256.
     *
     * OBS:
     * - O metodo signWith não aceita mais String diretamente (foi depreciado).
     * - Usar bytes garante mais segurança e compatibilidade com HMAC SHA-256.
     * - Reescrevi usando ajuda de IA para deixar mais entendivel e limpo"
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

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

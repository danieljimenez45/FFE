package com.entradas_cine.ffe.cine.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Genera y valida tokens JWT con la librería Auth0.
 * La caducidad del token se configura en segundos (por ejemplo 86400 = un día).
 */
@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Valor en segundos desde application.properties
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("Generando token JWT para usuario: {}", userDetails.getUsername());
        Date now = new Date();
        // jwtExpiration está en segundos; se convierte a milisegundos (* 1000)
        Date expiry = new Date(now.getTime() + jwtExpiration * 1000);

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    @Override
    public String extractUserName(String token) {
        log.info("Extrayendo username del token JWT");
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .build()
                .verify(token)
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withSubject(userDetails.getUsername())
                    .build()
                    .verify(token);
            log.info("Token JWT válido para usuario: {}", userDetails.getUsername());
            return !decoded.getExpiresAt().before(new Date());
        } catch (JWTVerificationException e) {
            log.warn("Token JWT no válido: {}", e.getMessage());
            return false;
        }
    }
}

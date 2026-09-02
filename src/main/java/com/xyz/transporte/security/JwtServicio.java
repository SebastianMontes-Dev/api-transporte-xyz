package com.xyz.transporte.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServicio {

    @Value("${seguridad.jwt.clave-secreta}")
    private String claveSecreta;

    @Value("${seguridad.jwt.expiracion-ms}")
    private long expiracionMs;

    public String generarToken(UserDetails detalles) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiracionMs);

        return Jwts.builder()
                .subject(detalles.getUsername())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(clave())
                .compact();
    }

    public String extraerUsuario(String token) {
        return parsearClaims(token).getSubject();
    }

    public boolean esValido(String token, UserDetails detalles) {
        String usuario = extraerUsuario(token);
        return usuario.equals(detalles.getUsername()) && !haExpirado(token);
    }

    private boolean haExpirado(String token) {
        return parsearClaims(token).getExpiration().before(new Date());
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey clave() {
        return Keys.hmacShaKeyFor(claveSecreta.getBytes(StandardCharsets.UTF_8));
    }
}

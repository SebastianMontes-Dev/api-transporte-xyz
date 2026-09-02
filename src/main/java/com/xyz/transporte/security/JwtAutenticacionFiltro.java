package com.xyz.transporte.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAutenticacionFiltro extends OncePerRequestFilter {

    private final JwtServicio jwtServicio;
    private final UserDetailsService usuarios;

    public JwtAutenticacionFiltro(JwtServicio jwtServicio, UserDetailsService usuarios) {
        this.jwtServicio = jwtServicio;
        this.usuarios = usuarios;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain cadena)
            throws ServletException, IOException {
        String cabecera = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            cadena.doFilter(request, response);
            return;
        }

        String token = cabecera.substring(7);

        try {
            String usuario = jwtServicio.extraerUsuario(token);

            if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails detalles = usuarios.loadUserByUsername(usuario);

                if (jwtServicio.esValido(token, detalles)) {
                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(detalles, null, detalles.getAuthorities());
                    autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            }
        } catch (JwtException | IllegalArgumentException excepcion) {
            SecurityContextHolder.clearContext();
        }

        cadena.doFilter(request, response);
    }
}

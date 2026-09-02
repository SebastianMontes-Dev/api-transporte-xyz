package com.xyz.transporte.config;

import com.xyz.transporte.security.JwtAutenticacionFiltro;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String SUPERVISOR = "SUPERVISOR";

    private final JwtAutenticacionFiltro jwtAutenticacionFiltro;

    public SeguridadConfig(JwtAutenticacionFiltro jwtAutenticacionFiltro) {
        this.jwtAutenticacionFiltro = jwtAutenticacionFiltro;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuracion) throws Exception {
        return configuracion.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint puntoEntradaNoAutenticado() {
        return (request, response, excepcion) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{\"estado\":401,\"mensaje\":\"Se requiere un token JWT valido\"}");
        };
    }

    @Bean
    public AccessDeniedHandler manejadorAccesoDenegado() {
        return (request, response, excepcion) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{\"estado\":403,\"mensaje\":\"No tiene permisos para acceder a este recurso\"}");
        };
    }

    @Bean
    public SecurityFilterChain cadenaSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(manejo -> manejo
                        .authenticationEntryPoint(puntoEntradaNoAutenticado())
                        .accessDeniedHandler(manejadorAccesoDenegado()))
                .authorizeHttpRequests(reglas -> reglas
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/camiones/*/conductor").hasRole(SUPERVISOR)
                        .requestMatchers(HttpMethod.POST, "/api/camiones", "/api/conductores").hasRole(ADMINISTRADOR)
                        .requestMatchers(HttpMethod.GET, "/api/camiones/**", "/api/conductores").hasAnyRole(ADMINISTRADOR, SUPERVISOR)
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAutenticacionFiltro, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

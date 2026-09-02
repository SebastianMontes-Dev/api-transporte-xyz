package com.xyz.transporte.config;

import com.xyz.transporte.security.JwtAutenticacionFiltro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String SUPERVISOR = "SUPERVISOR";

    @Value("${seguridad.administrador.usuario}")
    private String usuarioAdministrador;

    @Value("${seguridad.administrador.clave}")
    private String claveAdministrador;

    @Value("${seguridad.supervisor.usuario}")
    private String usuarioSupervisor;

    @Value("${seguridad.supervisor.clave}")
    private String claveSupervisor;

    private final JwtAutenticacionFiltro jwtAutenticacionFiltro;

    public SeguridadConfig(@Lazy JwtAutenticacionFiltro jwtAutenticacionFiltro) {
        this.jwtAutenticacionFiltro = jwtAutenticacionFiltro;
    }

    @Bean
    public PasswordEncoder codificadorClaves() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarios(PasswordEncoder codificador) {
        UserDetails administrador = User.withUsername(usuarioAdministrador)
                .password(codificador.encode(claveAdministrador))
                .roles(ADMINISTRADOR)
                .build();

        UserDetails supervisor = User.withUsername(usuarioSupervisor)
                .password(codificador.encode(claveSupervisor))
                .roles(SUPERVISOR)
                .build();

        return new InMemoryUserDetailsManager(administrador, supervisor);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuracion) throws Exception {
        return configuracion.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain cadenaSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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

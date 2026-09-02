package com.xyz.transporte.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UsuariosConfig {

    @Value("${seguridad.administrador.usuario}")
    private String usuarioAdministrador;

    @Value("${seguridad.administrador.clave}")
    private String claveAdministrador;

    @Value("${seguridad.supervisor.usuario}")
    private String usuarioSupervisor;

    @Value("${seguridad.supervisor.clave}")
    private String claveSupervisor;

    @Bean
    public PasswordEncoder codificadorClaves() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarios(PasswordEncoder codificador) {
        UserDetails administrador = User.withUsername(usuarioAdministrador)
                .password(codificador.encode(claveAdministrador))
                .roles(SeguridadConfig.ADMINISTRADOR)
                .build();

        UserDetails supervisor = User.withUsername(usuarioSupervisor)
                .password(codificador.encode(claveSupervisor))
                .roles(SeguridadConfig.SUPERVISOR)
                .build();

        return new InMemoryUserDetailsManager(administrador, supervisor);
    }
}

package com.xyz.transporte.controller;

import com.xyz.transporte.dto.LoginRespuesta;
import com.xyz.transporte.dto.LoginSolicitud;
import com.xyz.transporte.security.JwtServicio;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionControlador {

    private final AuthenticationManager autenticacionManager;
    private final UserDetailsService usuarios;
    private final JwtServicio jwtServicio;

    public AutenticacionControlador(AuthenticationManager autenticacionManager,
                                     UserDetailsService usuarios,
                                     JwtServicio jwtServicio) {
        this.autenticacionManager = autenticacionManager;
        this.usuarios = usuarios;
        this.jwtServicio = jwtServicio;
    }

    @PostMapping("/login")
    public LoginRespuesta login(@Valid @RequestBody LoginSolicitud solicitud) {
        autenticacionManager.authenticate(
                new UsernamePasswordAuthenticationToken(solicitud.usuario(), solicitud.clave()));

        UserDetails detalles = usuarios.loadUserByUsername(solicitud.usuario());
        return new LoginRespuesta(jwtServicio.generarToken(detalles));
    }
}

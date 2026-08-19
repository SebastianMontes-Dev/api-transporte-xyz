package com.xyz.transporte.controlador;

import com.xyz.transporte.dto.ConductorRespuesta;
import com.xyz.transporte.dto.ConductorSolicitud;
import com.xyz.transporte.servicio.ConductorServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conductores")
public class ConductorControlador {

    private final ConductorServicio servicio;

    public ConductorControlador(ConductorServicio servicio) {
        this.servicio = servicio;
    }

    // Solo el administrador (se controla en SeguridadConfig).
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConductorRespuesta registrar(@Valid @RequestBody ConductorSolicitud solicitud) {
        return servicio.registrar(solicitud);
    }

    @GetMapping
    public List<ConductorRespuesta> listar() {
        return servicio.listar();
    }
}

package com.xyz.transporte.controller;

import com.xyz.transporte.dto.AsignacionSolicitud;
import com.xyz.transporte.dto.CamionRespuesta;
import com.xyz.transporte.dto.CamionSolicitud;
import com.xyz.transporte.service.CamionServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
public class CamionControlador {

    private final CamionServicio servicio;

    public CamionControlador(CamionServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CamionRespuesta registrar(@Valid @RequestBody CamionSolicitud solicitud) {
        return servicio.registrar(solicitud);
    }

    @GetMapping
    public List<CamionRespuesta> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public CamionRespuesta consultar(@PathVariable Long id) {
        return servicio.consultar(id);
    }

    @PutMapping("/{id}/conductor")
    public CamionRespuesta asignarConductor(@PathVariable Long id,
                                            @Valid @RequestBody AsignacionSolicitud solicitud) {
        return servicio.asignarConductor(id, solicitud.conductorId());
    }
}

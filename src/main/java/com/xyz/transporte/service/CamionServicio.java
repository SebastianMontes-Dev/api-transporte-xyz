package com.xyz.transporte.service;

import com.xyz.transporte.dto.CamionRespuesta;
import com.xyz.transporte.dto.CamionSolicitud;
import com.xyz.transporte.exception.DatoDuplicadoExcepcion;
import com.xyz.transporte.exception.NoEncontradoExcepcion;
import com.xyz.transporte.model.Camion;
import com.xyz.transporte.model.Conductor;
import com.xyz.transporte.repository.CamionRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CamionServicio {

    private final CamionRepositorio repositorio;
    private final ConductorServicio conductorServicio;

    public CamionServicio(CamionRepositorio repositorio, ConductorServicio conductorServicio) {
        this.repositorio = repositorio;
        this.conductorServicio = conductorServicio;
    }

    public CamionRespuesta registrar(CamionSolicitud solicitud) {
        String placa = solicitud.placa().trim().toUpperCase();
        if (repositorio.existsByPlaca(placa)) {
            throw new DatoDuplicadoExcepcion("ya existe un camion con la placa " + placa);
        }
        Camion camion = new Camion();
        camion.setPlaca(placa);
        camion.setTipoVehiculo(solicitud.tipoVehiculo().trim());
        return convertir(repositorio.save(camion));
    }

    public List<CamionRespuesta> listar() {
        return repositorio.findAll().stream().map(CamionServicio::convertir).toList();
    }

    public CamionRespuesta consultar(Long id) {
        return convertir(buscar(id));
    }

    public CamionRespuesta asignarConductor(Long camionId, Long conductorId) {
        Camion camion = buscar(camionId);
        Conductor conductor = conductorServicio.buscar(conductorId);

        Optional<Camion> ocupado = repositorio.findByConductorId(conductorId);
        if (ocupado.isPresent() && !ocupado.get().getId().equals(camionId)) {
            throw new DatoDuplicadoExcepcion("el conductor ya esta asignado al camion con placa "
                    + ocupado.get().getPlaca());
        }

        camion.setConductor(conductor);
        return convertir(repositorio.save(camion));
    }

    private Camion buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new NoEncontradoExcepcion("no existe el camion con id " + id));
    }

    private static CamionRespuesta convertir(Camion camion) {
        return new CamionRespuesta(camion.getId(), camion.getPlaca(), camion.getTipoVehiculo(),
                ConductorServicio.convertir(camion.getConductor()));
    }
}

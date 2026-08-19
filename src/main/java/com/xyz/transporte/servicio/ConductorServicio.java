package com.xyz.transporte.servicio;

import com.xyz.transporte.dto.ConductorRespuesta;
import com.xyz.transporte.dto.ConductorSolicitud;
import com.xyz.transporte.excepcion.DatoDuplicadoExcepcion;
import com.xyz.transporte.excepcion.NoEncontradoExcepcion;
import com.xyz.transporte.modelo.Conductor;
import com.xyz.transporte.repositorio.ConductorRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConductorServicio {

    private final ConductorRepositorio repositorio;

    public ConductorServicio(ConductorRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public ConductorRespuesta registrar(ConductorSolicitud solicitud) {
        String documento = solicitud.documento().trim();
        if (repositorio.existsByDocumento(documento)) {
            throw new DatoDuplicadoExcepcion("ya existe un conductor con el documento " + documento);
        }
        Conductor conductor = new Conductor();
        conductor.setNombre(solicitud.nombre().trim());
        conductor.setDocumento(documento);
        conductor.setLicencia(solicitud.licencia().trim());
        return convertir(repositorio.save(conductor));
    }

    public List<ConductorRespuesta> listar() {
        return repositorio.findAll().stream().map(ConductorServicio::convertir).toList();
    }

    public Conductor buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new NoEncontradoExcepcion("no existe el conductor con id " + id));
    }

    public static ConductorRespuesta convertir(Conductor conductor) {
        if (conductor == null) {
            return null;
        }
        return new ConductorRespuesta(conductor.getId(), conductor.getNombre(),
                conductor.getDocumento(), conductor.getLicencia());
    }
}

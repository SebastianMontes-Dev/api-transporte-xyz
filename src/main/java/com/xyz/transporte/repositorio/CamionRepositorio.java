package com.xyz.transporte.repositorio;

import com.xyz.transporte.modelo.Camion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CamionRepositorio extends JpaRepository<Camion, Long> {

    boolean existsByPlaca(String placa);

    Optional<Camion> findByConductorId(Long conductorId);
}

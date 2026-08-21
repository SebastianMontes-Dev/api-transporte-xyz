package com.xyz.transporte.repository;

import com.xyz.transporte.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CamionRepositorio extends JpaRepository<Camion, Long> {

    boolean existsByPlaca(String placa);

    Optional<Camion> findByConductorId(Long conductorId);
}

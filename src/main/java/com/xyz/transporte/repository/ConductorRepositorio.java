package com.xyz.transporte.repository;

import com.xyz.transporte.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductorRepositorio extends JpaRepository<Conductor, Long> {

    boolean existsByDocumento(String documento);
}

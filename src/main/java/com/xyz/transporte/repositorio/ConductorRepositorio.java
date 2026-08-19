package com.xyz.transporte.repositorio;

import com.xyz.transporte.modelo.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductorRepositorio extends JpaRepository<Conductor, Long> {

    boolean existsByDocumento(String documento);
}

package com.xyz.transporte.dto;

import jakarta.validation.constraints.NotBlank;

public record ConductorSolicitud(
        @NotBlank(message = "el nombre es obligatorio") String nombre,
        @NotBlank(message = "el documento es obligatorio") String documento,
        @NotBlank(message = "la licencia es obligatoria") String licencia) {
}

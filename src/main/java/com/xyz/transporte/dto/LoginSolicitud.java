package com.xyz.transporte.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginSolicitud(
        @NotBlank(message = "El usuario es obligatorio") String usuario,
        @NotBlank(message = "La clave es obligatoria") String clave) {
}

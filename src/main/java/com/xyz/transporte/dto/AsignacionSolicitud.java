package com.xyz.transporte.dto;

import jakarta.validation.constraints.NotNull;

public record AsignacionSolicitud(
        @NotNull(message = "el id del conductor es obligatorio") Long conductorId) {
}

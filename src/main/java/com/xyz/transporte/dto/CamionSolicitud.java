package com.xyz.transporte.dto;

import jakarta.validation.constraints.NotBlank;

public record CamionSolicitud(
        @NotBlank(message = "la placa es obligatoria") String placa,
        @NotBlank(message = "el tipo de vehiculo es obligatorio") String tipoVehiculo) {
}

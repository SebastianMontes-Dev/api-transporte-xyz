package com.xyz.transporte.dto;

public record LoginRespuesta(String token, String tipo) {

    public LoginRespuesta(String token) {
        this(token, "Bearer");
    }
}

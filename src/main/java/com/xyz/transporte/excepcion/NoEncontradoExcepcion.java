package com.xyz.transporte.excepcion;

public class NoEncontradoExcepcion extends RuntimeException {

    public NoEncontradoExcepcion(String mensaje) {
        super(mensaje);
    }
}

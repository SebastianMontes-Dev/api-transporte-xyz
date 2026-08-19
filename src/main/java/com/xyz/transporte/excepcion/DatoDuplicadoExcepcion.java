package com.xyz.transporte.excepcion;

public class DatoDuplicadoExcepcion extends RuntimeException {

    public DatoDuplicadoExcepcion(String mensaje) {
        super(mensaje);
    }
}

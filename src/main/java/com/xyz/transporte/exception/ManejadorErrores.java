package com.xyz.transporte.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(NoEncontradoExcepcion.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(NoEncontradoExcepcion ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DatoDuplicadoExcepcion.class)
    public ResponseEntity<Map<String, Object>> duplicado(DatoDuplicadoExcepcion ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> credencialesInvalidas(AuthenticationException ex) {
        return respuesta(HttpStatus.UNAUTHORIZED, "Usuario o clave invalidos");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> datosInvalidos(MethodArgumentNotValidException ex) {
        StringBuilder detalle = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (detalle.length() > 0) {
                detalle.append("; ");
            }
            detalle.append(error.getDefaultMessage());
        });
        return respuesta(HttpStatus.BAD_REQUEST, detalle.toString());
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado, String mensaje) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", estado.value());
        cuerpo.put("mensaje", mensaje);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}

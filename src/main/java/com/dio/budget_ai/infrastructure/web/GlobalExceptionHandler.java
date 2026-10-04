package com.dio.budget_ai.infrastructure.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Erros causados por dados inválidos enviados pelo usuário.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarArgumentoInvalido(
            IllegalArgumentException exception) {

        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "erro",
                                exception.getMessage()
                        )
                );
    }

    /*
     * Erros relacionados a serviços externos,
     * como transcrição ou geração de áudio.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> tratarServicoIndisponivel(
            IllegalStateException exception) {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(
                        Map.of(
                                "erro",
                                exception.getMessage()
                        )
                );
    }

    /*
     * Tratamento genérico para erros inesperados.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> tratarErroGenerico(
            Exception exception) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        Map.of(
                                "erro",
                                "Ocorreu um erro interno na aplicação."
                        )
                );
    }
}
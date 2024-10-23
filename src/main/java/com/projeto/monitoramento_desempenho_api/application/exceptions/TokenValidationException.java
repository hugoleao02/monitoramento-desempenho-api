package com.projeto.monitoramento_desempenho_api.application.exceptions;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException(String message) {
        super(message);
    }

}

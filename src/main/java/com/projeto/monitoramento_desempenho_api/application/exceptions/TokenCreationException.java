package com.projeto.monitoramento_desempenho_api.application.exceptions;

public class TokenCreationException extends RuntimeException {
    public TokenCreationException(String message) {
        super(message);
    }
}

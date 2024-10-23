package com.projeto.monitoramento_desempenho_api.application.exceptions;

public class UserAlreadyExistsException extends  RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

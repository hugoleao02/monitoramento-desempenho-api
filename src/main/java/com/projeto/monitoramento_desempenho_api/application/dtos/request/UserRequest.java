package com.projeto.monitoramento_desempenho_api.application.dtos.request;


import java.util.UUID;

public record UserRequest(
        UUID id,
        String name,
        String email,
        String password,
        boolean active,
        String role
) {
}
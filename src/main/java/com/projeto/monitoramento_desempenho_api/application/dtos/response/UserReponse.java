package com.projeto.monitoramento_desempenho_api.application.dtos.response;

import java.util.UUID;

public record UserReponse(UUID id,
                          String name,
                          String email,
                          boolean active,
                          String role) {

}

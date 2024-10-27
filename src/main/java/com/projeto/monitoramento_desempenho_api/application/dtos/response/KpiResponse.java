package com.projeto.monitoramento_desempenho_api.application.dtos.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record KpiResponse(
        UUID id,
        String name,
        String description,
        String calculationFormula,
        Float target,
        String measurementUnit,
        String category,
        LocalDateTime creationDate,
        UUID userId
) {
}

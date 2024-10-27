package com.projeto.monitoramento_desempenho_api.application.exceptions;

import java.util.UUID;

public class KpiNotFoundException extends RuntimeException {
    public KpiNotFoundException(UUID id) {
        super("KPI not found with ID: " + id);
    }
}
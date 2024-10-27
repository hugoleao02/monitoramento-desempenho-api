package com.projeto.monitoramento_desempenho_api.application.exceptions;

public class KpiAlreadyExistsException extends RuntimeException {
    public KpiAlreadyExistsException(String kpiName) {
        super("KPI with name '" + kpiName + "' already exists.");
    }
}
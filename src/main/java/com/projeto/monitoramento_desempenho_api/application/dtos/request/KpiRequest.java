package com.projeto.monitoramento_desempenho_api.application.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record KpiRequest(
        UUID id,
        @NotBlank(message = "Name is mandatory.")
        String name,
        @NotBlank(message = "Description is mandatory.")
        String description,
        @NotBlank(message = "Calculation formula is mandatory.")
        @Pattern(regexp = "^[0-9+\\-*/()\\s]+$", message = "Calculation formula contains invalid characters.")
        String calculationFormula,
        @NotNull(message = "Target is mandatory.")
        Float target,
        @NotBlank(message = "Measurement unit is mandatory.")
        String measurementUnit,
        @NotBlank(message = "Category is mandatory.")
        String category,
        @NotNull(message = "User ID is mandatory.")
        UUID userId
) {}

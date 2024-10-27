package com.projeto.monitoramento_desempenho_api.domain.entities;

import com.projeto.monitoramento_desempenho_api.enums.KpiCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "kpis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KPI {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @Column(name = "calculation_formula")
    private String calculationFormula;

    private Float target;

    @Column(name = "measurement_unit")
    private String measurementUnit;

    @Enumerated(EnumType.STRING)
    private KpiCategory category;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @NotNull(message = "User ID is mandatory.")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ElementCollection
    @Column(name = "shared_with_users")
    private List<UUID> sharedWithUsers;

    @Column(name = "calculated_value")
    private Double calculatedValue;

    public void shareWith(UUID userId) {
        if (sharedWithUsers != null && !sharedWithUsers.contains(userId)) {
            sharedWithUsers.add(userId);
        }
    }
}

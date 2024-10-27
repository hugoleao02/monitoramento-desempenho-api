package com.projeto.monitoramento_desempenho_api.infra.repositories;

import com.projeto.monitoramento_desempenho_api.domain.entities.KPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KpiRepository extends JpaRepository<KPI, UUID> {
    List<KPI> findByUserId(UUID userId);

    Optional<KPI> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
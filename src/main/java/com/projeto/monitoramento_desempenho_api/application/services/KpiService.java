package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.KpiRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.KpiResponse;
import com.projeto.monitoramento_desempenho_api.application.exceptions.KpiNotFoundException;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UnauthorizedAccessException;
import com.projeto.monitoramento_desempenho_api.application.exceptions.ValidationException;
import com.projeto.monitoramento_desempenho_api.domain.entities.KPI;
import com.projeto.monitoramento_desempenho_api.enums.KpiCategory;
import com.projeto.monitoramento_desempenho_api.infra.repositories.KpiRepository;
import com.projeto.monitoramento_desempenho_api.application.mappers.KpiMapper;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class KpiService {

    private final KpiRepository kpiRepository;
    private final KpiMapper kpiMapper;

    @Autowired
    public KpiService(KpiRepository kpiRepository, KpiMapper kpiMapper) {
        this.kpiRepository = kpiRepository;
        this.kpiMapper = kpiMapper;
    }

    @Transactional
    public KpiResponse createKpi(KpiRequest kpiRequest, UUID userId) {
        validateKpiRequest(kpiRequest);

        KPI kpi = kpiMapper.toEntity(kpiRequest);
        kpi.setCreationDate(LocalDateTime.now());
        kpi.setUserId(userId);

        double calculatedValue = calculateKpiValue(kpi.getCalculationFormula());
        kpi.setCalculatedValue(calculatedValue);

        kpi = kpiRepository.save(kpi);
        return kpiMapper.toResponse(kpi);
    }

    private void validateKpiRequest(KpiRequest kpiRequest) {
        if (kpiRequest.name().isEmpty() || kpiRequest.description().isEmpty()) {
            throw new ValidationException("All fields are required.");
        }
        if (!isValidCalculationFormula(kpiRequest.calculationFormula())) {
            throw new ValidationException("Invalid calculation formula.");
        }
    }

    public boolean isValidCalculationFormula(String formula) {
        return formula != null && !formula.isEmpty();
    }

    private double calculateKpiValue(String formula) {
        try {
            Expression expression = new ExpressionBuilder(formula).build();
            return expression.evaluate();
        } catch (Exception e) {
            throw new ValidationException("Failed to evaluate the formula: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<KpiResponse> getAllKpisByUser(UUID userId) {
        List<KPI> kpis = kpiRepository.findByUserId(userId);
        return kpiMapper.toResponseList(kpis);
    }

    @Transactional(readOnly = true)
    public KpiResponse getKpiById(UUID id, UUID userId) {
        KPI kpi = kpiRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new KpiNotFoundException(id));
        return kpiMapper.toResponse(kpi);
    }

    @Transactional
    public KpiResponse updateKpi(UUID id, KpiRequest kpiRequest, UUID userId) {
        validateKpiRequest(kpiRequest);

        KPI kpi = kpiRepository.findById(id)
                .orElseThrow(() -> new KpiNotFoundException(id));

        if (!kpi.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to update this KPI.");
        }

        kpi.setName(kpiRequest.name());
        kpi.setDescription(kpiRequest.description());
        kpi.setCalculationFormula(kpiRequest.calculationFormula());
        kpi.setTarget(kpiRequest.target());
        kpi.setMeasurementUnit(kpiRequest.measurementUnit());
        kpi.setCategory(KpiCategory.valueOf(kpiRequest.category()));

        double calculatedValue = calculateKpiValue(kpi.getCalculationFormula());
        kpi.setCalculatedValue(calculatedValue);

        kpi = kpiRepository.save(kpi);
        return kpiMapper.toResponse(kpi);
    }

    @Transactional
    public void deleteKpi(UUID id, UUID userId) {
        if (!kpiRepository.existsByIdAndUserId(id, userId)) {
            throw new KpiNotFoundException(id);
        }
        kpiRepository.deleteById(id);
    }

    @Transactional
    public void shareKpi(UUID kpiId, UUID userId, UUID targetUserId) {
        KPI kpi = kpiRepository.findById(kpiId)
                .orElseThrow(() -> new KpiNotFoundException(kpiId));

        if (!kpi.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to share this KPI.");
        }

        kpi.shareWith(targetUserId);
        kpiRepository.save(kpi);
    }
}

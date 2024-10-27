package com.projeto.monitoramento_desempenho_api.web.controllers;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.KpiRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.KpiResponse;
import com.projeto.monitoramento_desempenho_api.application.services.KpiService;
import com.projeto.monitoramento_desempenho_api.application.exceptions.KpiNotFoundException;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UnauthorizedAccessException;
import com.projeto.monitoramento_desempenho_api.application.exceptions.ValidationException;
import com.projeto.monitoramento_desempenho_api.infra.security.UserAuthenticated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/kpis")
public class KpiController {

    private final KpiService kpiService;

    public KpiController(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    @PostMapping
    public ResponseEntity<KpiResponse> createKpi(@RequestBody KpiRequest kpiRequest,
                                                 @AuthenticationPrincipal UserAuthenticated userAuthenticated) {
        UUID userId = userAuthenticated.user().getId();
        KpiResponse createdKpi = kpiService.createKpi(kpiRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdKpi);
    }

    @GetMapping
    public ResponseEntity<List<KpiResponse>> getAllKpis(@AuthenticationPrincipal UserAuthenticated userAuthenticated) {
        UUID userId = userAuthenticated.user().getId();
        List<KpiResponse> kpis = kpiService.getAllKpisByUser(userId);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KpiResponse> getKpiById(@PathVariable UUID id,
                                                  @AuthenticationPrincipal UserAuthenticated userAuthenticated) {
        UUID userId = userAuthenticated.user().getId();
        KpiResponse kpi = kpiService.getKpiById(id, userId);
        return ResponseEntity.ok(kpi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KpiResponse> updateKpi(@PathVariable UUID id,
                                                 @RequestBody KpiRequest kpiRequest,
                                                 @AuthenticationPrincipal UserAuthenticated userAuthenticated) {
        UUID userId = userAuthenticated.user().getId();
        KpiResponse updatedKpi = kpiService.updateKpi(id, kpiRequest, userId);
        return ResponseEntity.ok(updatedKpi);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKpi(@PathVariable UUID id,
                                          @AuthenticationPrincipal UserAuthenticated userAuthenticated) {
        UUID userId = userAuthenticated.user().getId();
        kpiService.deleteKpi(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/share/{targetUserId}")
    public ResponseEntity<Void> shareKpi(@PathVariable UUID id,
                                         @PathVariable UUID targetUserId,
                                         @AuthenticationPrincipal UserAuthenticated userAuthenticated) {
        UUID userId = userAuthenticated.user().getId();
        kpiService.shareKpi(id, userId, targetUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

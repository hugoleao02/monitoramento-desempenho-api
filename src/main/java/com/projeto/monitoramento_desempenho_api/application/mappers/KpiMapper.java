package com.projeto.monitoramento_desempenho_api.application.mappers;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.KpiRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.KpiResponse;
import com.projeto.monitoramento_desempenho_api.domain.entities.KPI;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface KpiMapper { ;

    KPI toEntity(KpiRequest kpiRequest);

    @Mapping(target = "creationDate", source = "creationDate")
    KpiResponse toResponse(KPI kpi);

    List<KpiResponse> toResponseList(List<KPI> kpis);
}

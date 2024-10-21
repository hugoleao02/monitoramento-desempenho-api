package com.projeto.monitoramento_desempenho_api.application.mappers;

import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserLoginDTO;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserRegisterDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterDTO dto);
    User toUser(UserLoginDTO dto);
}

package com.projeto.monitoramento_desempenho_api.application.mappers;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.UserRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.UserReponse; // Importando UserReponse
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserRegisterDTO;
import com.projeto.monitoramento_desempenho_api.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserRegisterDTO userRegisterDTO);

    @Mapping(target = "role", source = "role", qualifiedByName = "stringToUserRole")
    User toUser(UserRequest request);

    @Named("stringToUserRole")
    default UserRole mapStringToUserRole(String role) {
        return UserRole.valueOf(role.toUpperCase());
    }

    UserRequest toUserRequest(User user);

    UserReponse toUserReponse(User user);
}

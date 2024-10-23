package com.projeto.monitoramento_desempenho_api.infra.repositories;

import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

}

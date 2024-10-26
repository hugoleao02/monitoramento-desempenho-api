package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import com.projeto.monitoramento_desempenho_api.infra.security.UserAuthenticated;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository repository;

    public AuthorizationService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return new UserAuthenticated(user);
    }

}

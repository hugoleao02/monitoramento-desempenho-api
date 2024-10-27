package com.projeto.monitoramento_desempenho_api.infra.security;

import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserAuthenticated(User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getRole()));
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Pode implementar lógica para verificar a expiração da conta
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Pode implementar lógica para verificar se a conta está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Pode implementar lógica para verificar se as credenciais estão expiradas
    }

    @Override
    public boolean isEnabled() {
        return user.isActive(); // Certifique-se de que o método isActive() existe em User
    }
}

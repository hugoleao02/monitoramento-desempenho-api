package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.LoginResponse;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserLoginDTO;
import com.projeto.monitoramento_desempenho_api.infra.config.security.TokenService;
import com.projeto.monitoramento_desempenho_api.infra.config.security.UserAuthenticated;
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceLoginTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve realizar o login com sucesso e retornar um token")
    void shouldLoginUserSuccessfully() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("test@example.com", "password123");

        Authentication authentication = mock(Authentication.class);
        UserAuthenticated userAuthenticated = mock(UserAuthenticated.class);
        User user = new User();
        when(authentication.getPrincipal()).thenReturn(userAuthenticated);
        when(userAuthenticated.user()).thenReturn(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(user)).thenReturn("token123");

        LoginResponse response = authService.loginUser(userLoginDTO);

        assertEquals("token123", response.token());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("Deve lançar BadCredentialsException quando as credenciais estiverem incorretas")
    void shouldThrowBadCredentialsExceptionWhenLoginFails() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("wrong@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> authService.loginUser(userLoginDTO));

        verify(tokenService, never()).generateToken(any(User.class));
    }


}

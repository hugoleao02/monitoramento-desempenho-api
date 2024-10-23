package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.UserRegisterDTO;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UserAlreadyExistsException;
import com.projeto.monitoramento_desempenho_api.application.mappers.UserMapper;
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceRegisterTest {


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void shouldRegisterUserSuccessfully() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Test User", "test@example.com", "password123");

        when(userRepository.findByEmail(userRegisterDTO.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRegisterDTO.password())).thenReturn("encodedPassword");

        User user = new User();
        when(userMapper.toUser(userRegisterDTO)).thenReturn(user);

        authService.registerUser(userRegisterDTO);

        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Test User", "test@example.com", "password123");

        when(userRepository.findByEmail(userRegisterDTO.email())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(userRegisterDTO));
        verify(userRepository, never()).save(any(User.class));
    }
}

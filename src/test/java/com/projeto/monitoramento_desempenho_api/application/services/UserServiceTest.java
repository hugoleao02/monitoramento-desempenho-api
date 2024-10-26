package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.UserRequest;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UserAlreadyExistsException;
import com.projeto.monitoramento_desempenho_api.application.mappers.UserMapper;
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.enums.UserRole;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    private UserRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new UserRequest("UserName", "user@test.com", "password123",true, UserRole.USER);
        user = mock(User.class);

        when(user.getEmail()).thenReturn("user@test.com");
        when(user.getPassword()).thenReturn("password123");
        when(user.getName()).thenReturn("UserName");
        when(user.isActive()).thenReturn(true);
        when(user.getRole()).thenReturn(UserRole.USER);
        when(user.getCreatedAt()).thenReturn(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail já existir")
    void create_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));
        verify(userRepository, times(1)).findByEmail(request.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve salvar o usuário quando o e-mail não existir")
    void create_ShouldSaveUser_WhenEmailDoesNotExist() {
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userMapper.toUser(request)).thenReturn(user);

        userService.create(request);

        verify(userRepository, times(1)).findByEmail(request.email());
        verify(userMapper, times(1)).toUser(request);
        verify(userRepository, times(1)).save(user);
    }
}

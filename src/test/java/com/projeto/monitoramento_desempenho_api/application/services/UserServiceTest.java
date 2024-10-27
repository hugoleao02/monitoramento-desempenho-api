package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.UserRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.UserReponse;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UserAlreadyExistsException;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UserNotFoundException;
import com.projeto.monitoramento_desempenho_api.application.mappers.UserMapper;
import com.projeto.monitoramento_desempenho_api.application.services.UserService;
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.enums.UserRole;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes para UserService")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRequest = new UserRequest(UUID.randomUUID(), "Nome", "email@exemplo.com", "senha", true, UserRole.USER.getName());
        user = new User();
        user.setId(userRequest.id());
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setActive(userRequest.active());
        user.setRole(UserRole.USER);
    }

    @Test
    @DisplayName("Deve lançar UserAlreadyExistsException ao tentar criar um usuário que já existe")
    void testCreateUser_UserAlreadyExists() {
        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.of(user));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.create(userRequest);
        });
        assertEquals("User with email email@exemplo.com already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void testCreateUser_Success() {
        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.empty());
        when(userMapper.toUser(any(UserRequest.class))).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");

        userService.create(userRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(userRequest.email(), userCaptor.getValue().getEmail());
        assertEquals("encryptedPassword", userCaptor.getValue().getPassword());
    }

    @Test
    @DisplayName("Deve retornar todos os usuários com sucesso")
    void testGetAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toUserReponse(any())).thenReturn(new UserReponse(user.getId(), user.getName(), user.getEmail(), user.isActive(), UserRole.USER.getName()));

        var users = userService.getAll();

        assertEquals(1, users.size());
        assertEquals(user.getEmail(), users.get(0).email());
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o usuário não é encontrado pelo ID")
    void testGetById_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).toUserReponse(any(User.class));
    }

    @Test
    @DisplayName("Deve retornar o usuário pelo ID com sucesso")
    void testGetById_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserReponse(any())).thenReturn(new UserReponse(user.getId(), user.getName(),
                user.getEmail(), user.isActive(), UserRole.USER.getName()));

        UserReponse response = userService.getById(user.getId());

        assertEquals(user.getEmail(), response.email());
    }

    @Test
    @DisplayName("Deve atualizar o usuário com sucesso")
    void testUpdateUser_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUser(any(UserRequest.class))).thenReturn(user);

        userService.update(userRequest);

        verify(userRepository).save(user);
        assertEquals(userRequest.name(), user.getName());
    }

    @Test
    @DisplayName("Deve deletar o usuário com sucesso")
    void testDeleteUser_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.delete(user.getId());

        verify(userRepository).delete(user);
    }
}

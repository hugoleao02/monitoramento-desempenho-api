package com.projeto.monitoramento_desempenho_api.web;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.UserRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.UserReponse;
import com.projeto.monitoramento_desempenho_api.application.services.UserService;
import com.projeto.monitoramento_desempenho_api.web.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserRequest userRequest;
    private UserReponse userReponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRequest = new UserRequest(UUID.randomUUID(), "Nome", "email@exemplo.com", "senha", true, "ROLE_USER");
        userReponse = new UserReponse(userRequest.id(), userRequest.name(), userRequest.email(), userRequest.active(), userRequest.role());
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso e retornar status 201")
    void testCreateUser_Success() {
        ResponseEntity<String> response = userController.createUser(userRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Usuário criado com sucesso.", response.getBody());
    }

    @Test
    @DisplayName("Deve retornar todos os usuários com status 200")
    void testGetAllUsers_Success() {
        when(userService.getAll()).thenReturn(Collections.singletonList(userReponse));

        ResponseEntity<List<UserReponse>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(userReponse.email(), response.getBody().get(0).email());
    }

    @Test
    @DisplayName("Deve retornar o usuário pelo ID com status 200")
    void testGetUserById_Success() {
        UUID userId = userRequest.id();
        when(userService.getById(userId)).thenReturn(userReponse);

        ResponseEntity<UserReponse> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userReponse.email(), Objects.requireNonNull(response.getBody()).email());
    }

    @Test
    @DisplayName("Deve atualizar o usuário com sucesso e retornar status 200")
    void testUpdateUser_Success() {
        ResponseEntity<String> response = userController.updateUser(userRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário atualizado com sucesso.", response.getBody());
    }

    @Test
    @DisplayName("Deve deletar o usuário com sucesso e retornar status 200")
    void testDeleteUser_Success() {
        UUID userId = userRequest.id();
        ResponseEntity<String> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário deletado com sucesso.", response.getBody());
    }
}

package com.projeto.monitoramento_desempenho_api.web;

import com.projeto.monitoramento_desempenho_api.application.dtos.LoginResponse;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserLoginDTO;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserRegisterDTO;
import com.projeto.monitoramento_desempenho_api.application.services.AuthService;
import com.projeto.monitoramento_desempenho_api.web.controllers.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("testUser", "test@example.com", "password123");

        ResponseEntity<String> response = authController.register(userRegisterDTO);


        verify(authService, times(1)).registerUser(userRegisterDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário registrado com sucesso.", response.getBody());
    }

    @Test
    void testLoginUser_Success() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("test@example.com", "password123");
        LoginResponse loginResponse = new LoginResponse("mockedToken");

        when(authService.loginUser(userLoginDTO)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(userLoginDTO);

        verify(authService, times(1)).loginUser(userLoginDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponse, response.getBody());
    }

    @Test
    void testLoginUser_Failure() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("invalid@example.com", "wrongPassword");

        when(authService.loginUser(userLoginDTO)).thenThrow(new RuntimeException("Credenciais inválidas"));

        try {
            authController.login(userLoginDTO);
        } catch (RuntimeException e) {
            assertEquals("Credenciais inválidas", e.getMessage());
        }
        verify(authService, times(1)).loginUser(userLoginDTO);
    }
}

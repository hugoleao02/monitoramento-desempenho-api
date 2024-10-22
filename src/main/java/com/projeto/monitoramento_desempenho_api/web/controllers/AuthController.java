package com.projeto.monitoramento_desempenho_api.web.controllers;

import com.projeto.monitoramento_desempenho_api.application.dtos.LoginResponse;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserLoginDTO;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserRegisterDTO;
import com.projeto.monitoramento_desempenho_api.application.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        authService.registerUser(userRegisterDTO);
        return ResponseEntity.ok("Usuário registrado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginDTO userLoginDTO) {
        LoginResponse loginResponse = authService.loginUser(userLoginDTO);
        return ResponseEntity.ok(loginResponse);
    }
}

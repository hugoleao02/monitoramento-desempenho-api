package com.projeto.monitoramento_desempenho_api.web.controllers;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.UserRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.UserReponse;
import com.projeto.monitoramento_desempenho_api.application.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest request) {
        try {
            userService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserReponse>> getAllUsers() {
        List<UserReponse> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReponse> getUserById(@PathVariable UUID id) {
        try {
            UserReponse userReponse = userService.getById(id);
            return ResponseEntity.ok(userReponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest request) {
        try {
            userService.update(request);
            return ResponseEntity.ok("Usuário atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok("Usuário deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}

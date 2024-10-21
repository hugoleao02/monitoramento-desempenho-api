package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.UserLoginDTO;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserRegisterDTO;
import com.projeto.monitoramento_desempenho_api.application.mappers.UserMapper;
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(UserRegisterDTO userRegisterDTO){
        userRepository.findByEmail(userRegisterDTO.email())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Email já cadastrado.");
                });

        User user = userMapper.toUser(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));
        userRepository.save(user);
    }

    public String loginUser(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!passwordEncoder.matches(userLoginDTO.password(), user.getPassword())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        return "Login bem-sucedido!";
    }

}

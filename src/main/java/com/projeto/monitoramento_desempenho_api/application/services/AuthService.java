package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.response.LoginResponse;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserLoginDTO;
import com.projeto.monitoramento_desempenho_api.application.dtos.UserRegisterDTO;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UserAlreadyExistsException;
import com.projeto.monitoramento_desempenho_api.application.mappers.UserMapper;
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.enums.UserRole;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import com.projeto.monitoramento_desempenho_api.infra.security.TokenService;
import com.projeto.monitoramento_desempenho_api.infra.security.UserAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userRegisterDTO.email());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Email já cadastrado.");
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.password());

        User newUser = userMapper.toUser(userRegisterDTO);
        newUser.setPassword(encryptedPassword);
        newUser.setRole(UserRole.ADMIN);

        userRepository.save(newUser);
    }

    public LoginResponse loginUser(UserLoginDTO userLoginDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userLoginDTO.email(), userLoginDTO.password()
        );

        var authentication = authenticationManager.authenticate(authToken);

        UserAuthenticated userAuthenticated = (UserAuthenticated) authentication.getPrincipal();

        User user = userAuthenticated.user();

        String token = tokenService.generateToken(user);

        return new LoginResponse(token);
    }

}

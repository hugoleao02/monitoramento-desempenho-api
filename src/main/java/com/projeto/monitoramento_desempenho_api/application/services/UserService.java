package com.projeto.monitoramento_desempenho_api.application.services;

import com.projeto.monitoramento_desempenho_api.application.dtos.request.UserRequest;
import com.projeto.monitoramento_desempenho_api.application.dtos.response.UserReponse; // Importando UserReponse
import com.projeto.monitoramento_desempenho_api.application.exceptions.UserAlreadyExistsException;
import com.projeto.monitoramento_desempenho_api.application.exceptions.UserNotFoundException;
import com.projeto.monitoramento_desempenho_api.application.mappers.UserMapper;
import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.enums.UserRole;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(UserRequest request) {
        boolean emailExists = userRepository.findByEmail(request.email()).isPresent();
        if (emailExists) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists.");
        }

        User newUser = userMapper.toUser(request);
        newUser.setPassword("123"); // Password default
        String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encryptedPassword);
        userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public List<UserReponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserReponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserReponse getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));

        return userMapper.toUserReponse(user);
    }

    @Transactional
    public void update(UserRequest request) {
        User userFound = userRepository.findById(request.id()).get();

        userFound.setName(request.name());
        userFound.setEmail(request.email());
        userFound.setActive(request.active());
        userFound.setRole(UserRole.valueOf(request.role().toUpperCase()));

        userRepository.save(userFound);
    }

    @Transactional
    public void delete(UUID id) {
        User userFound = userRepository.findById(id).get();

        userRepository.delete(userFound);
    }
}

package com.projeto1cc.grevia.user.service;


import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.mapper.UserMapper;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.Status;
import com.projeto1cc.grevia.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toUser(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setStatus(Status.Active);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(savedUser);
    }

    @Transactional
    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO userRequestDTO) {
        return userRepository.findById(id).map(user -> {
            user.setName(userRequestDTO.name());
            user.setEmail(userRequestDTO.email());
            if (userRequestDTO.password() != null && !userRequestDTO.password().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
            }
            user.setRole(userRequestDTO.role());
            User updatedUser = userRepository.save(user);
            return userMapper.toUserResponseDTO(updatedUser);
        });
    }

    @Transactional
    public void deactivateUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatus(Status.Inactive);
            userRepository.save(user);
        });
    }

    @Transactional
    public Optional<UserResponseDTO> findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toUserResponseDTO);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        // Lógica para gerar um token de reset, salvar no banco e enviar por e-mail
        // Ex:
        // 1. Encontrar o usuário pelo e-mail.
        // 2. Gerar um token único e com tempo de expiração.
        // 3. Salvar o token associado ao usuário.
        // 4. Enviar um e-mail para o usuário com o link de reset contendo o token.
        System.out.println("Solicitação de reset de senha para: " + email);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        // Lógica para validar o token e redefinir a senha
        // Ex:
        // 1. Encontrar o usuário associado ao token.
        // 2. Verificar se o token é válido e não expirou.
        // 3. Criptografar e atualizar a nova senha.
        // 4. Invalidar o token.
        System.out.println("Redefinindo senha com o token: " + token);
    }
}

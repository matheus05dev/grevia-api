package com.projeto1cc.grevia.user.service;


import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.dto.ChangePasswordRequestDTO;
import com.projeto1cc.grevia.user.mapper.UserMapper;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;
import com.projeto1cc.grevia.user.repository.UserRepository;
import com.projeto1cc.grevia.core.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Não é possível criar a conta com este e-mail.");
        }
        User user = userMapper.toUser(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setStatus(Status.Active);
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        log.info("Novo usuário registrado com sucesso: {}", savedUser.getEmail());
        return userMapper.toUserResponseDTO(savedUser);
    }

    @Transactional
    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO userRequestDTO) {
        return userRepository.findById(id).map(user -> {
            if (!user.getEmail().equals(userRequestDTO.email()) &&
                userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
                throw new IllegalArgumentException("Não é possível criar a conta com este e-mail.");
            }
            user.setName(userRequestDTO.name());
            user.setEmail(userRequestDTO.email());
            if (userRequestDTO.password() != null && !userRequestDTO.password().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
                log.info("Senha alterada para o usuário: {}", user.getEmail());
            }
            // Role is NOT updated here for security reasons
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
    public Optional<UserResponseDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUserResponseDTO);
    }

    @Transactional
    public Optional<UserResponseDTO> updateUserByEmail(String email, UserRequestDTO dto) {
        return userRepository.findByEmail(email).map(user -> {
            user.setName(dto.name());
            User updatedUser = userRepository.save(user);
            return userMapper.toUserResponseDTO(updatedUser);
        });
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            userRepository.delete(user);
            log.info("Usuário excluído permanentemente: {}", email);
        });
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequestDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
        log.info("Senha alterada com sucesso para o usuário: {}", email);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = java.util.UUID.randomUUID().toString();
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(java.time.LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
            log.info("E-mail de recuperação de senha enviado com sucesso para: {}", email);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        userRepository.findByResetPasswordToken(token).ifPresentOrElse(user -> {
            if (user.getResetPasswordTokenExpiry() != null &&
                user.getResetPasswordTokenExpiry().isAfter(java.time.LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetPasswordToken(null);
                user.setResetPasswordTokenExpiry(null);
                userRepository.save(user);
                log.info("Senha redefinida com sucesso utilizando o token: {}", token);
            } else {
                log.warn("Tentativa falha de redefinição: Token de recuperação expirado ({})", token);
                throw new IllegalArgumentException("Token de recuperação de senha inválido ou expirado.");
            }
        }, () -> {
            log.warn("Tentativa falha de redefinição: Token de recuperação inválido ({})", token);
            throw new IllegalArgumentException("Token de recuperação de senha inválido ou expirado.");
        });
    }

    @Transactional
    public void promoteToAdmin(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        });
    }
}

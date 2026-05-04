package com.projeto1cc.grevia.user.service;

import com.projeto1cc.grevia.core.service.EmailService;
import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.mapper.UserMapper;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;
import com.projeto1cc.grevia.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO("Test", "test@test.com", "password", null, null);
        user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@test.com");
    }

    @Test
    void createUser_ShouldEncodePasswordAndSetDefaultRoleAndStatus() {
        when(userMapper.toUser(userRequestDTO)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(new UserResponseDTO(user.getName(), user.getEmail(), Role.USER, Status.Active, null, 0, 0, 0));

        UserResponseDTO response = userService.createUser(userRequestDTO);

        assertEquals("encoded_password", user.getPassword());
        assertEquals(Role.USER, user.getRole());
        assertEquals(Status.Active, user.getStatus());
        verify(userRepository, times(1)).save(user);
        assertNotNull(response);
    }

    @Test
    void requestPasswordReset_ShouldGenerateTokenAndSendEmail() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        userService.requestPasswordReset("test@test.com");

        assertNotNull(user.getResetPasswordToken());
        assertNotNull(user.getResetPasswordTokenExpiry());
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendPasswordResetEmail(eq(user.getEmail()), anyString());
    }

    @Test
    void resetPassword_ShouldUpdatePasswordWhenTokenIsValid() {
        String token = "valid_token";
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));

        when(userRepository.findByResetPasswordToken(token)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encoded_new_password");

        userService.resetPassword(token, "newPassword");

        assertEquals("encoded_new_password", user.getPassword());
        assertNull(user.getResetPasswordToken());
        assertNull(user.getResetPasswordTokenExpiry());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetPassword_ShouldThrowExceptionWhenTokenIsExpired() {
        String token = "expired_token";
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().minusHours(1));

        when(userRepository.findByResetPasswordToken(token)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.resetPassword(token, "newPassword"));
        verify(userRepository, never()).save(user);
    }

    @Test
    void promoteToAdmin_ShouldSetRoleToAdmin() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.promoteToAdmin(1L);

        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository, times(1)).save(user);
    }
    
    @Test
    void deactivateUser_ShouldSetStatusToInactive() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        assertEquals(Status.Inactive, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }
}

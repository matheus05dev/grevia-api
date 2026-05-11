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
    void createUser_ShouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.findByEmail(userRequestDTO.email())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userRequestDTO);
        });

        assertEquals("Não é possível criar a conta com este e-mail.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
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
    void deleteUserByEmail_ShouldCallDelete() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        userService.deleteUserByEmail("test@test.com");

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void changePassword_ShouldUpdatePasswordWhenCurrentIsCorrect() {
        com.projeto1cc.grevia.user.dto.ChangePasswordRequestDTO dto = new com.projeto1cc.grevia.user.dto.ChangePasswordRequestDTO("oldPass", "newPass");
        user.setPassword("encodedOldPass");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        userService.changePassword("test@test.com", dto);

        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_ShouldThrowExceptionWhenCurrentIsIncorrect() {
        com.projeto1cc.grevia.user.dto.ChangePasswordRequestDTO dto = new com.projeto1cc.grevia.user.dto.ChangePasswordRequestDTO("wrongPass", "newPass");
        user.setPassword("encodedOldPass");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedOldPass")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.changePassword("test@test.com", dto));
        verify(userRepository, never()).save(user);
    }
}

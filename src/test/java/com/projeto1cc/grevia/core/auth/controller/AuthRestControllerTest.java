package com.projeto1cc.grevia.core.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto1cc.grevia.core.auth.dto.ForgotPasswordRequestDTO;
import com.projeto1cc.grevia.core.auth.dto.LoginRequestDTO;
import com.projeto1cc.grevia.core.auth.dto.ResetPasswordRequestDTO;
import com.projeto1cc.grevia.core.auth.service.JwtService;
import com.projeto1cc.grevia.core.auth.service.RefreshTokenService;
import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;
import com.projeto1cc.grevia.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthRestController.class)
@AutoConfigureMockMvc(addFilters = false) // Ignora as configurações de segurança globais para o teste unitário
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @Test
    void register_ShouldReturn200AndUserResponse() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO("Test", "test@test.com", "password", null, null);
        UserResponseDTO responseDTO = new UserResponseDTO("Test", "test@test.com", Role.USER, Status.Active, null, 0, 0, 0, "🌱 Jardineiro Iniciante", "🌱", 1);

        Mockito.when(userService.createUser(any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_ShouldReturn200AndJwtToken() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@test.com", "password");
        
        User mockUser = new User();
        mockUser.setEmail("test@test.com");
        mockUser.setPassword("encoded");
        mockUser.setRole(Role.USER);
        mockUser.setStatus(Status.Active);
        
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
               .thenReturn(mockAuthentication);
        
        Mockito.when(jwtService.generateToken(any(UserDetails.class))).thenReturn("mocked_jwt_token");

        com.projeto1cc.grevia.core.auth.model.RefreshToken mockRefreshToken = new com.projeto1cc.grevia.core.auth.model.RefreshToken();
        mockRefreshToken.setToken("mocked_refresh_token");
        Mockito.when(refreshTokenService.createRefreshToken("test@test.com")).thenReturn(mockRefreshToken);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked_jwt_token"));
    }

    @Test
    void forgotPassword_ShouldReturn200() throws Exception {
        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO("test@test.com");

        Mockito.doNothing().when(userService).requestPasswordReset(anyString());

        mockMvc.perform(post("/api/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        Mockito.verify(userService, Mockito.times(1)).requestPasswordReset("test@test.com");
    }

    @Test
    void resetPassword_ShouldReturn200() throws Exception {
        ResetPasswordRequestDTO request = new ResetPasswordRequestDTO("valid_token", "new_password");

        Mockito.doNothing().when(userService).resetPassword(anyString(), anyString());

        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).resetPassword("valid_token", "new_password");
    }
}

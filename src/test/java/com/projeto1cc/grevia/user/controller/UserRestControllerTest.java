package com.projeto1cc.grevia.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;
import com.projeto1cc.grevia.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.projeto1cc.grevia.core.auth.service.JwtService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false) // Ignore global security to focus on controller logic
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "test@test.com")
    void getMyProfile_ShouldReturn200AndProfile() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO("Test", "test@test.com", Role.USER, Status.Active, null, 0, 0, 0);

        Mockito.when(userService.findUserByEmail("test@test.com")).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @WithMockUser(username = "notfound@test.com")
    void getMyProfile_ShouldReturn404WhenNotFound() throws Exception {
        Mockito.when(userService.findUserByEmail("notfound@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void updateMyProfile_ShouldReturn200AndUpdatedProfile() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO("Updated Name", null, null, null, null);
        UserResponseDTO responseDTO = new UserResponseDTO("Updated Name", "test@test.com", Role.USER, Status.Active, null, 0, 0, 0);

        Mockito.when(userService.updateUserByEmail(eq("test@test.com"), any(UserRequestDTO.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void deactivateMyAccount_ShouldReturn204() throws Exception {
        Mockito.doNothing().when(userService).deactivateUserByEmail("test@test.com");

        mockMvc.perform(delete("/api/users/me"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deactivateUserByEmail("test@test.com");
    }

    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ADMIN"})
    void promoteUser_ShouldReturn200() throws Exception {
        Mockito.doNothing().when(userService).promoteToAdmin(1L);

        // Even with filters disabled, we test if the endpoint is called and returns OK
        mockMvc.perform(patch("/api/users/1/promote"))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).promoteToAdmin(1L);
    }
}

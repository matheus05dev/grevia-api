package com.projeto1cc.grevia.user.controller;

import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    // GET /api/users/me — Returns the authenticated user's own profile
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        String email = getAuthenticatedEmail();
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/users/me — Updates the authenticated user's own profile
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(@RequestBody UserRequestDTO userRequestDTO) {
        String email = getAuthenticatedEmail();
        return userService.updateUserByEmail(email, userRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/users/me — Deactivates the authenticated user's own account
    @DeleteMapping("/me")
    public ResponseEntity<Void> deactivateMyAccount() {
        String email = getAuthenticatedEmail();
        userService.deactivateUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/users/{id}/promote — Promotes a user to ADMIN (Admin only)
    @PatchMapping("/{id}/promote")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> promoteUser(@PathVariable Long id) {
        userService.promoteToAdmin(id);
        return ResponseEntity.ok().build();
    }

    private String getAuthenticatedEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}

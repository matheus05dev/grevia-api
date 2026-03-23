package com.projeto1cc.grevia.core.auth.controller;


import com.projeto1cc.grevia.core.auth.dto.LoginRequestDTO;
import com.projeto1cc.grevia.core.auth.dto.LoginResponseDTO;
import com.projeto1cc.grevia.core.auth.service.JwtService;
import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.projeto1cc.grevia.core.auth.dto.ForgotPasswordRequestDTO;
import com.projeto1cc.grevia.core.auth.dto.ResetPasswordRequestDTO;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        userService.requestPasswordReset(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        userService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok().build();
    }
}

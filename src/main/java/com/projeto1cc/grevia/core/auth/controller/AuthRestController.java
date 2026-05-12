package com.projeto1cc.grevia.core.auth.controller;


import com.projeto1cc.grevia.core.auth.dto.LoginRequestDTO;
import com.projeto1cc.grevia.core.auth.dto.LoginResponseDTO;
import com.projeto1cc.grevia.core.auth.model.RefreshToken;
import com.projeto1cc.grevia.core.auth.service.JwtService;
import com.projeto1cc.grevia.core.auth.service.RefreshTokenService;
import com.projeto1cc.grevia.user.dto.UserRequestDTO;
import com.projeto1cc.grevia.user.dto.UserResponseDTO;
import com.projeto1cc.grevia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.projeto1cc.grevia.core.auth.dto.ForgotPasswordRequestDTO;
import com.projeto1cc.grevia.core.auth.dto.ResetPasswordRequestDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private static final String REFRESH_COOKIE_NAME = "grevia_refresh_token";
    private static final Duration REFRESH_COOKIE_MAX_AGE = Duration.ofDays(7);

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO request) {
        log.info("Iniciando requisição de registro para o e-mail: {}", request.email());
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

        // Create refresh token and set as HttpOnly cookie
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.email());
        ResponseCookie cookie = buildRefreshCookie(refreshToken.getToken(), REFRESH_COOKIE_MAX_AGE);

        log.info("Login bem-sucedido para o usuário: {}", request.email());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponseDTO(jwt));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest request) {
        String refreshTokenValue = extractRefreshCookie(request);
        if (refreshTokenValue == null) {
            log.warn("Tentativa de refresh sem cookie");
            return ResponseEntity.status(401).build();
        }

        return refreshTokenService.findByToken(refreshTokenValue)
                .map(existingToken -> {
                    if (refreshTokenService.isTokenExpired(existingToken)) {
                        refreshTokenService.deleteByToken(refreshTokenValue);
                        log.warn("Refresh token expirado para o usuário: {}", existingToken.getUser().getEmail());
                        ResponseCookie clearCookie = buildRefreshCookie("", Duration.ZERO);
                        return ResponseEntity.status(401)
                                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                                .<LoginResponseDTO>build();
                    }

                    // Rotate refresh token
                    RefreshToken newRefreshToken = refreshTokenService.rotateToken(existingToken);

                    // Generate new access token
                    UserDetails userDetails = userDetailsService.loadUserByUsername(
                            existingToken.getUser().getEmail()
                    );
                    String newJwt = jwtService.generateToken(userDetails);

                    ResponseCookie cookie = buildRefreshCookie(newRefreshToken.getToken(), REFRESH_COOKIE_MAX_AGE);

                    log.info("Token renovado com sucesso para o usuário: {}", existingToken.getUser().getEmail());
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body(new LoginResponseDTO(newJwt));
                })
                .orElseGet(() -> {
                    log.warn("Refresh token não encontrado no banco");
                    return ResponseEntity.status(401).build();
                });
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String refreshTokenValue = extractRefreshCookie(request);
        if (refreshTokenValue != null) {
            refreshTokenService.deleteByToken(refreshTokenValue);
        }

        ResponseCookie clearCookie = buildRefreshCookie("", Duration.ZERO);
        log.info("Logout realizado com sucesso");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        log.info("Iniciando requisição de recuperação de senha para: {}", request.email());
        userService.requestPasswordReset(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        log.info("Iniciando requisição de redefinição de senha com token recebido");
        userService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok().build();
    }

    // ── Helpers ──

    private ResponseCookie buildRefreshCookie(String value, Duration maxAge) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth/refresh")
                .maxAge(maxAge)
                .build();
    }

    private String extractRefreshCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> REFRESH_COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}

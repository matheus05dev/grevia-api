package com.projeto1cc.grevia.core.auth.service;

import com.projeto1cc.grevia.core.auth.model.RefreshToken;
import com.projeto1cc.grevia.core.auth.repository.RefreshTokenRepository;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_DURATION_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public RefreshToken createRefreshToken(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Remove old tokens for this user
        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_DURATION_DAYS, ChronoUnit.DAYS));

        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token criado para o usuário: {}", userEmail);
        return saved;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    @Transactional
    public RefreshToken rotateToken(RefreshToken oldToken) {
        RefreshToken newToken = new RefreshToken();
        newToken.setUser(oldToken.getUser());
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_DURATION_DAYS, ChronoUnit.DAYS));

        refreshTokenRepository.delete(oldToken);
        RefreshToken saved = refreshTokenRepository.save(newToken);
        log.info("Refresh token rotacionado para o usuário: {}", oldToken.getUser().getEmail());
        return saved;
    }

    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            refreshTokenRepository.delete(rt);
            log.info("Refresh token invalidado para o usuário: {}", rt.getUser().getEmail());
        });
    }

    @Transactional
    public void deleteByUserEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user ->
            refreshTokenRepository.deleteByUserId(user.getId())
        );
    }
}

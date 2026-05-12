package com.projeto1cc.grevia.core.auth.service;

import com.projeto1cc.grevia.core.auth.model.RefreshToken;
import com.projeto1cc.grevia.core.auth.repository.RefreshTokenRepository;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
    }

    @Test
    void createRefreshToken_ShouldDeleteOldTokensAndCreateNew() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        RefreshToken result = refreshTokenService.createRefreshToken("test@test.com");

        verify(refreshTokenRepository).deleteByUserId(1L);
        assertNotNull(result.getToken());
        assertEquals(user, result.getUser());
        assertTrue(result.getExpiryDate().isAfter(Instant.now()));
    }

    @Test
    void createRefreshToken_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> refreshTokenService.createRefreshToken("nonexistent@test.com"));
    }

    @Test
    void isTokenExpired_ShouldReturnTrue_WhenExpired() {
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().minus(1, ChronoUnit.HOURS));

        assertTrue(refreshTokenService.isTokenExpired(token));
    }

    @Test
    void isTokenExpired_ShouldReturnFalse_WhenValid() {
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plus(1, ChronoUnit.DAYS));

        assertFalse(refreshTokenService.isTokenExpired(token));
    }

    @Test
    void rotateToken_ShouldDeleteOldAndCreateNewWithSameUser() {
        RefreshToken oldToken = new RefreshToken();
        oldToken.setId(1L);
        oldToken.setToken("old-uuid");
        oldToken.setUser(user);
        oldToken.setExpiryDate(Instant.now().plus(1, ChronoUnit.DAYS));

        when(refreshTokenRepository.save(any(RefreshToken.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        RefreshToken newToken = refreshTokenService.rotateToken(oldToken);

        verify(refreshTokenRepository).delete(oldToken);
        assertNotNull(newToken.getToken());
        assertNotEquals("old-uuid", newToken.getToken());
        assertEquals(user, newToken.getUser());
        assertTrue(newToken.getExpiryDate().isAfter(Instant.now()));
    }

    @Test
    void deleteByToken_ShouldDeleteWhenFound() {
        RefreshToken token = new RefreshToken();
        token.setToken("some-uuid");
        token.setUser(user);

        when(refreshTokenRepository.findByToken("some-uuid")).thenReturn(Optional.of(token));

        refreshTokenService.deleteByToken("some-uuid");

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteByToken_ShouldDoNothing_WhenNotFound() {
        when(refreshTokenRepository.findByToken("nonexistent")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> refreshTokenService.deleteByToken("nonexistent"));
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void findByToken_ShouldReturnToken() {
        RefreshToken token = new RefreshToken();
        token.setToken("test-uuid");

        when(refreshTokenRepository.findByToken("test-uuid")).thenReturn(Optional.of(token));

        Optional<RefreshToken> result = refreshTokenService.findByToken("test-uuid");

        assertTrue(result.isPresent());
        assertEquals("test-uuid", result.get().getToken());
    }
}

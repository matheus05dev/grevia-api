package com.projeto1cc.grevia.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimitingFilterTest {

    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        // Set a fixed IP for tests
        request.setRemoteAddr("192.168.0.1");
    }

    @Test
    void doFilterInternal_ShouldAllowRequestWhenWithinAuthLimit() throws ServletException, IOException {
        request.setRequestURI("/api/auth/register");

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus()); // Default MockHttpServletResponse status
    }

    @Test
    void doFilterInternal_ShouldBlockRequestWhenAuthLimitExceeded() throws ServletException, IOException {
        request.setRequestURI("/api/auth/register");

        // The auth limit is 30 requests. We will do 30 successful requests.
        for (int i = 0; i < 30; i++) {
            rateLimitingFilter.doFilterInternal(request, response, filterChain);
        }
        
        verify(filterChain, times(30)).doFilter(request, response);

        // The 31st request should be blocked.
        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();
        rateLimitingFilter.doFilterInternal(request, blockedResponse, filterChain);

        // Filter chain should not be called a 31st time
        verify(filterChain, times(30)).doFilter(request, response);
        assertEquals(429, blockedResponse.getStatus());
    }

    @Test
    void doFilterInternal_ShouldAllowRequestWhenWithinGeneralLimit() throws ServletException, IOException {
        request.setRequestURI("/api/plants/1");

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());
    }
}

package com.projeto1cc.grevia.core.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limiting filter using the Token Bucket algorithm (Bucket4j).
 *
 * Rules (per IP):
 *  - /api/auth/**  → max 30 requests / 15 minutes (brute-force/credential stuffing protection)
 *  - All other    → max 300 req/min  AND  2000 req/hour  (DDoS + burst protection, two layers)
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // Auth limits
    private static final int AUTH_LIMIT_CAPACITY = 30;
    private static final Duration AUTH_LIMIT_DURATION = Duration.ofMinutes(15);

    // General limits
    private static final int GENERAL_LIMIT_PER_MINUTE = 300;
    private static final int GENERAL_LIMIT_PER_HOUR = 2000;

    private final ConcurrentHashMap<String, Bucket> authBuckets    = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bucket> generalBuckets = new ConcurrentHashMap<>();

    // 30 tokens refilled every 15 minutes — auth endpoints
    private Bucket newAuthBucket() {
        Bandwidth limit = Bandwidth.classic(AUTH_LIMIT_CAPACITY, Refill.intervally(AUTH_LIMIT_CAPACITY, AUTH_LIMIT_DURATION));
        return Bucket.builder().addLimit(limit).build();
    }
 
    // Two-layer protection for general endpoints:
    //   Layer 1 → 300 req/min   (burst protection)
    //   Layer 2 → 2000 req/hour (slow DDoS / scraping protection)
    private Bucket newGeneralBucket() {
        Bandwidth perMinute = Bandwidth.classic(GENERAL_LIMIT_PER_MINUTE, Refill.intervally(GENERAL_LIMIT_PER_MINUTE, Duration.ofMinutes(1)));
        Bandwidth perHour   = Bandwidth.classic(GENERAL_LIMIT_PER_HOUR, Refill.intervally(GENERAL_LIMIT_PER_HOUR, Duration.ofHours(1)));
        return Bucket.builder()
                .addLimit(perMinute)
                .addLimit(perHour)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String ip = getClientIp(request);
        String path = request.getRequestURI();

        Bucket bucket;
        if (path.startsWith("/api/auth")) {
            bucket = authBuckets.computeIfAbsent(ip, k -> newAuthBucket());
        } else {
            bucket = generalBuckets.computeIfAbsent(ip, k -> newGeneralBucket());
        }

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            
            // Se o login for bem-sucedido, reseta o rate limit de autenticação para esse IP
            if ("/api/auth/login".equals(path) && response.getStatus() == HttpStatus.OK.value()) {
                authBuckets.remove(ip);
            }
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"erro\": \"Muitas requisições em pouco tempo. Por favor, aguarde um momento e tente novamente.\"}"
            );
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

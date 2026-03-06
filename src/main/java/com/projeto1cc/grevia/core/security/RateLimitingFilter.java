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
 *  - /api/auth/**  → max 5 requests / 15 minutes (brute-force/credential stuffing protection)
 *  - All other    → max 60 req/min  AND  500 req/hour  (DDoS + burst protection, two layers)
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, Bucket> authBuckets    = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bucket> generalBuckets = new ConcurrentHashMap<>();

    // 5 tokens refilled every 15 minutes — auth endpoints (brute-force / credential-stuffing protection)
    // interval refill = tokens are added at a constant rate, not all at once, preventing burst exploitation
    private Bucket newAuthBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(15)));
        return Bucket.builder().addLimit(limit).build();
    }

    // Two-layer protection for general endpoints:
    //   Layer 1 → 60 req/min   (burst protection)
    //   Layer 2 → 500 req/hour (slow DDoS / scraping protection)
    private Bucket newGeneralBucket() {
        Bandwidth perMinute = Bandwidth.classic(60,  Refill.intervally(60,  Duration.ofMinutes(1)));
        Bandwidth perHour   = Bandwidth.classic(500, Refill.intervally(500, Duration.ofHours(1)));
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

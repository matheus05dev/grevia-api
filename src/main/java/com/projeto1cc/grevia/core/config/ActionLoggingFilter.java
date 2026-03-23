package com.projeto1cc.grevia.core.config;

import com.projeto1cc.grevia.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ActionLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        
        filterChain.doFilter(request, response);
        
        long duration = System.currentTimeMillis() - startTime;
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = "Anônimo";
        
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                userName = user.getName();
            } else {
                userName = authentication.getName(); 
            }
        }
        
        log.info("Ação: [{}] {} | Usuário (Nome): {} | Status: {} | Tempo: {}ms", 
                request.getMethod(), 
                request.getRequestURI(), 
                userName, 
                response.getStatus(), 
                duration);
    }
}

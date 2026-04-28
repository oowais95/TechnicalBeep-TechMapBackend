package com.technicalbeep.techeventsmap.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Placeholder for a future JWT bearer filter. Currently performs no authentication and only
 * delegates the chain. Replace body with token parsing / {@code SecurityContext} population when
 * Spring Security (or a custom auth layer) is introduced.
 */
@Component
public class JwtAuthenticationFilterPlaceholder extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilterPlaceholder.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("JWT placeholder: {} {}", request.getMethod(), request.getRequestURI());
        }
        // TODO: read Authorization: Bearer <jwt>, validate signature/expiry, set SecurityContext
        filterChain.doFilter(request, response);
    }
}

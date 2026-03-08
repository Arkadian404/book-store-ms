package org.zafu.orderservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class InternalServiceAuthFilter extends OncePerRequestFilter {

    @Value("${security.internal.secret-key}")
    private String internalServiceKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        boolean targetEndpoint = path != null && path.startsWith("/api/v1/orders/code/");

        if (targetEndpoint && SecurityContextHolder.getContext().getAuthentication() == null) {
            String serviceKey = request.getHeader("X-Internal-Service-Key");
            if (serviceKey.equals(internalServiceKey)) {
                UsernamePasswordAuthenticationToken internalAuth = new UsernamePasswordAuthenticationToken(
                        "internal-service",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_INTERNAL_SERVICE"))
                );
                SecurityContextHolder.getContext().setAuthentication(internalAuth);
            }
        }
        filterChain.doFilter(request, response);
    }
}

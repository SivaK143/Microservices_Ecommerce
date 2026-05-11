package com.ecommerce.filter;

import com.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Read Authorization header
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        // No token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return chain.filter(exchange);
        }

        // Extract token
        String token = authHeader.substring(7);

        // Extract username
        String username = jwtService.extractUsername(token);
        System.out.println("Username from JWT "+ username);

        // Validate token
        if (!jwtService.isTokenValid(token, username)) {
            return chain.filter(exchange);
        }

        //extract role from token
        String role = jwtService.extractRole(token);
        System.out.println("Role from JWT "+ role);

        // Create authentication object
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of((new SimpleGrantedAuthority("ROLE_" + role)))
                );

        // Set authentication in SecurityContext
        SecurityContextImpl securityContext =
                new SecurityContextImpl(authentication);

        return chain.filter(exchange)
                .contextWrite(
                        ReactiveSecurityContextHolder
                                .withSecurityContext(Mono.just(securityContext))
                );
    }

    }

package com.ecommerce.config;

import com.ecommerce.exception.ForbiddenHandler;
import com.ecommerce.exception.UnauthorizedHandler;
import com.ecommerce.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UnauthorizedHandler unauthorizedHandler;
    private final ForbiddenHandler forbiddenHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http.
                csrf(csrf->csrf.disable())
                .authorizeExchange(exchange -> exchange
                        //Public APIs
                        .pathMatchers("/ecommerce/auth/**").permitAll()
                        //product API's
                        .pathMatchers(HttpMethod.GET, "/ecommerce/products/**").hasAnyRole("USER","ADMIN")
                        .pathMatchers(HttpMethod.POST, "/ecommerce/products/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/ecommerce/products/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/ecommerce/products/**").hasRole("ADMIN")

                        // // CATEGORY APIs
                        .pathMatchers(HttpMethod.GET,"/ecommerce/categories/**").hasAnyRole("ADMIN", "USER")
                        .pathMatchers(HttpMethod.POST,"/ecommerce/categories/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/ecommerce/categories/**").hasRole("ADMIN")
                         .pathMatchers(HttpMethod.DELETE, "/ecommerce/categories/**").hasRole("ADMIN")
                        //Order APIS
                        .pathMatchers(HttpMethod.POST, "/ecommerce/orders/**").hasAnyRole("USER","ADMIN")
                        .pathMatchers(HttpMethod.GET, "/ecommerce/orders/My").hasRole("USER")
                        .pathMatchers(HttpMethod.GET, "/ecommerce/orders").hasRole("ADMIN")
                        //All other are protected
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                //disable basic login form
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .exceptionHandling(ex->
                        ex.authenticationEntryPoint(unauthorizedHandler)
                                .accessDeniedHandler(forbiddenHandler));
               return http.build();
    }
}

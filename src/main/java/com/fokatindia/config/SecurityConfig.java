package com.fokatindia.config;

import com.fokatindia.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http
    ) {

        return http

                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // ENABLE CORS
                .cors(cors -> {})
                .authorizeExchange(exchange -> exchange

                        .pathMatchers(
                                "/restful/v1/api/users/**",
                                "/restful/v1/api/role-permissions",
                                "/restful/v1/api/permissions/**",
                                "/restful/v1/api/user-roles/**",
                                "/restful/v1/api/roles/**",
                                "/internal/**",
                                "/restful/v1/api/users/admin/vendors",
                                "/restful/v1/api/users/admin/subvendors",
                                "/restful/v1/api/vendors/**",
                                "/restful/v1/api/subvendors/**",
                                "/restful/v1/api/documents/**",
                                "/restful/v1/api/categories/**",
                                "/restful/v1/api/services",
                                "/restful/v1/api/bookings/**",
                                "/restful/v1/api/notifications/**",
                                "/restful/v1/api/payments/**",
                                "/restful/v1/api/reviews/**"
                        )

                        .permitAll()

                        .anyExchange()

                        .authenticated()
                )

                .addFilterAt(
                        jwtFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )

                .build();
    }
}
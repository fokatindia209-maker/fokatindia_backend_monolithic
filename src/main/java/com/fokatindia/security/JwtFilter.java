package com.fokatindia.security;

import com.fokatindia.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final RoleRepository roleRepository;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            if (jwtTokenProvider.validateToken(token)) {

                String mobileNumber =
                        jwtTokenProvider
                                .getMobileNumberFromToken(token);

                Long userId =
                        jwtTokenProvider
                                .getUserIdFromToken(token);

                return roleRepository
                        .findPermissionsByUserId(userId)
                        .collectList()

                        .flatMap(permissions -> {

                            List<SimpleGrantedAuthority> authorities =
                                    permissions.stream()

                                            .map(SimpleGrantedAuthority::new)

                                            .collect(Collectors.toList());

                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(
                                            mobileNumber,
                                            null,
                                            authorities
                                    );

                            return chain.filter(exchange)
                                    .contextWrite(
                                            ReactiveSecurityContextHolder
                                                    .withAuthentication(auth)
                                    );
                        });
            }
        }

        return chain.filter(exchange);
    }
}
//package com.fokatindia.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class JwtFilter implements WebFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    private final PermissionCacheService permissionCacheService;
//
//    @Override
//    public Mono<Void> filter(
//            ServerWebExchange exchange,
//            WebFilterChain chain
//    ) {
//
//        String authHeader = exchange.getRequest()
//                .getHeaders()
//                .getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader != null &&
//                authHeader.startsWith("Bearer ")) {
//
//            String token = authHeader.substring(7);
//
//            if (jwtTokenProvider.validateToken(token)) {
//
//                String mobileNumber =
//                        jwtTokenProvider
//                                .getMobileNumberFromToken(token);
//
//                Long userId =
//                        jwtTokenProvider
//                                .getUserIdFromToken(token);
//
//                return permissionCacheService
//                        .getPermissions(userId)
//
//                        .flatMap(permissions -> {
//
//                            List<SimpleGrantedAuthority> authorities =
//                                    permissions.stream()
//
//                                            .map(SimpleGrantedAuthority::new)
//
//                                            .collect(Collectors.toList());
//
//                            UsernamePasswordAuthenticationToken auth =
//                                    new UsernamePasswordAuthenticationToken(
//                                            mobileNumber,
//                                            null,
//                                            authorities
//                                    );
//
//                            return chain.filter(exchange)
//                                    .contextWrite(
//                                            ReactiveSecurityContextHolder
//                                                    .withAuthentication(auth)
//                                    );
//                        });
//            }
//        }
//
//        return chain.filter(exchange);
//    }
//}
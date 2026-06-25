package com.fokatindia.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-1)   // 🔥 ensures filter runs early
public class LoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().getPath();

        log.info("➡️ Request: {} {}", method, path);

        return chain.filter(exchange)
                .doOnSuccess(done -> {
                    String status = String.valueOf(exchange.getResponse().getStatusCode());
                    log.info("⬅️ Response: {} {}", method, status);
                });
    }
}

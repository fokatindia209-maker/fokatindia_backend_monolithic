package com.fokatindia.repository;

import com.fokatindia.entity.Token;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TokenRepository extends ReactiveCrudRepository<Token, Long> {
    Mono<Token> findByUserId(Long userId);
}

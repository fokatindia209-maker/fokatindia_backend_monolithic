package com.fokatindia.repository;

import com.fokatindia.entity.Token;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TokenRepository extends ReactiveCrudRepository<Token, Long> {
    Mono<Token> findByUserId(Long userId);

    @Query("SELECT * FROM tokens WHERE user_id IN (:userIds)")
    Flux<Token> findByUserIds(List<Long> userIds);

    Flux<Token> findAll();
}

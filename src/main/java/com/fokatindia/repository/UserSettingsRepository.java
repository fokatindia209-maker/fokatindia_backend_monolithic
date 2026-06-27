package com.fokatindia.repository;

import com.fokatindia.entity.UserSettings;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserSettingsRepository extends ReactiveCrudRepository<UserSettings, Long> {
    Mono<UserSettings> findByUserId(Long userId);
}

package com.fokatindia.service.notification;

import com.fokatindia.dto.notification.NotificationRequest;
import com.fokatindia.dto.notification.NotificationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {
    Mono<NotificationResponse> sendNotification(NotificationRequest request);
    Flux<NotificationResponse> getAll();

    Mono<NotificationResponse> getById(Long id);

    Mono<NotificationResponse> update(
            Long id,
            NotificationRequest request
    );

    Mono<NotificationResponse> markAsRead(Long id);

    Mono<Void> delete(Long id);
}

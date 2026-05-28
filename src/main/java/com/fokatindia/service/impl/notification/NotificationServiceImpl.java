package com.fokatindia.service.impl.notification;


import com.fokatindia.dto.notification.NotificationRequest;
import com.fokatindia.dto.notification.NotificationResponse;
import com.fokatindia.entity.notification.Notifications;
import com.fokatindia.repository.notification.NotificationRepository;
import com.fokatindia.service.notification.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    // =====================================================
    // SEND NOTIFICATION
    // =====================================================
    @Override
    public Mono<NotificationResponse> sendNotification(
            NotificationRequest request
    ) {

        Notifications notification = new Notifications();

        notification.setUserId(request.getUserId());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setReadStatus(false);
        notification.setCreatedAt(LocalDateTime.now());

        return repository.save(notification)

                .flatMap(saved -> {

                    // SEND FIREBASE PUSH
                    if (request.getFcmToken() != null &&
                            !request.getFcmToken().isBlank()) {

                        Message message = Message.builder()

                                .setToken(request.getFcmToken())

                                .setNotification(
                                        com.google.firebase.messaging.Notification
                                                .builder()
                                                .setTitle(request.getTitle())
                                                .setBody(request.getMessage())
                                                .build()
                                )

                                .putData("type", request.getType())

                                .build();

                        return Mono.fromCallable(() -> {

                            FirebaseMessaging
                                    .getInstance()
                                    .send(message);

                            return mapToResponse(saved);
                        });
                    }

                    return Mono.just(mapToResponse(saved));
                });
    }

    // =====================================================
    // GET ALL
    // =====================================================
    @Override
    public Flux<NotificationResponse> getAll() {

        return repository.findAll()

                .map(this::mapToResponse);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @Override
    public Mono<NotificationResponse> getById(Long id) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException(
                                        "Notification not found"
                                )
                        )
                )

                .map(this::mapToResponse);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @Override
    public Mono<NotificationResponse> update(
            Long id,
            NotificationRequest request
    ) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException(
                                        "Notification not found"
                                )
                        )
                )

                .flatMap(notification -> {



                    if (request.getTitle() != null) {
                        notification.setTitle(request.getTitle());
                    }

                    if (request.getMessage() != null) {
                        notification.setMessage(request.getMessage());
                    }

                    if (request.getType() != null) {
                        notification.setType(request.getType());
                    }

                    return repository.save(notification);
                })

                .map(this::mapToResponse);
    }

    // =====================================================
    // MARK AS READ
    // =====================================================
    @Override
    public Mono<NotificationResponse> markAsRead(Long id) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException(
                                        "Notification not found"
                                )
                        )
                )

                .flatMap(notification -> {

                    notification.setReadStatus(true);

                    return repository.save(notification);
                })

                .map(this::mapToResponse);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @Override
    public Mono<Void> delete(Long id) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException(
                                        "Notification not found"
                                )
                        )
                )

                .flatMap(repository::delete);
    }

    // =====================================================
    // MAPPER
    // =====================================================
    private NotificationResponse mapToResponse(
            Notifications notification
    ) {

        NotificationResponse response =
                new NotificationResponse();

        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setReadStatus(notification.getReadStatus());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }
}
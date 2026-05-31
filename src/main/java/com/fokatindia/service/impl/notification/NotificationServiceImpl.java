package com.fokatindia.service.impl.notification;


import com.fokatindia.dto.notification.NotificationRequest;
import com.fokatindia.dto.notification.NotificationResponse;
import com.fokatindia.entity.Token;
import com.fokatindia.entity.notification.Notifications;
import com.fokatindia.repository.TokenRepository;
import com.fokatindia.repository.notification.NotificationRepository;
import com.fokatindia.service.FirebaseService;
import com.fokatindia.service.notification.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final TokenRepository tokenRepository;
    private final FirebaseService firebase;

    public NotificationServiceImpl(NotificationRepository repository, TokenRepository tokenRepository, FirebaseService firebase) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.firebase = firebase;
    }

    // =====================================================
    // SEND NOTIFICATION
    // =====================================================

    @Override
    public Mono<NotificationResponse> sendNotification(NotificationRequest request) {

        Notifications notification = new Notifications();
        notification.setUserId(request.getUserId());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setReadStatus(false);
        notification.setActive(true);
        notification.setCreatedAt(LocalDateTime.now());

        return repository.save(notification)
                .flatMap(saved -> {

                    // 🔥 CASE 1: SEND TO ALL USERS
                    if (Boolean.TRUE.equals(request.getSendToAll())) {
                        return tokenRepository.findAll()
                                .map(Token::getFcmToken)
                                .filter(Objects::nonNull)
                                .distinct()
                                .flatMap(token ->
                                        firebase.send(
                                                token,
                                                request.getTitle(),
                                                request.getMessage(),
                                                request.getType()
                                        )
                                )
                                .then()
                                .thenReturn(map(saved));
                    }

                    // ================================
                    // 2. MULTIPLE USERS
                    // ================================
                    if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
                        return tokenRepository.findByUserIds(request.getUserIds())
                                .map(Token::getFcmToken)
                                .filter(Objects::nonNull)
                                .distinct()
                                .flatMap(token ->
                                        firebase.send(
                                                token,
                                                request.getTitle(),
                                                request.getMessage(),
                                                request.getType()
                                        )
                                )
                                .then()
                                .thenReturn(map(saved));
                    }

                    // ================================
                    // 3. SINGLE USER (userId)
                    // ================================
                    if (request.getUserId() != null) {
                        return tokenRepository.findByUserId(request.getUserId())
                                .map(Token::getFcmToken)
                                .filter(Objects::nonNull)
                                .flatMap(token ->
                                        firebase.send(
                                                token,
                                                request.getTitle(),
                                                request.getMessage(),
                                                request.getType()
                                        )
                                )
                                .thenReturn(map(saved));
                    }

                    // ================================
                    // 4. DIRECT TOKEN
                    // ================================
                    if (request.getFcmToken() != null) {
                        return firebase.send(
                                request.getFcmToken(),
                                request.getTitle(),
                                request.getMessage(),
                                request.getType()
                        ).thenReturn(map(saved));
                    }


                    return Mono.just(map(saved));
                });
    }

    // =====================================================
    // GET ALL
    // =====================================================
    @Override
    public Flux<NotificationResponse> getAll() {

        return repository.findAll()

                .map(this::map);
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

                .map(this::map);
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

                .map(this::map);
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

                .map(this::map);
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
    private NotificationResponse map(Notifications n) {

        NotificationResponse r = new NotificationResponse();
        r.setId(n.getId());
        r.setUserId(n.getUserId());
        r.setTitle(n.getTitle());
        r.setMessage(n.getMessage());
        r.setType(n.getType());
        r.setReadStatus(n.getReadStatus());
        r.setActive(n.getActive());
        r.setCreatedAt(n.getCreatedAt());
        r.setUpdatedAt(n.getUpdatedAt());
        return r;
    }
}
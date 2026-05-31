package com.fokatindia.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FirebaseService {

    public Mono<Void> send(String token, String title, String message, String type) {

        return Mono.fromRunnable(() -> {
            try {

                Message msg = Message.builder()
                        .setToken(token)
                        .setNotification(
                                com.google.firebase.messaging.Notification.builder()
                                        .setTitle(title)
                                        .setBody(message)
                                        .build()
                        )
                        .putData("type", type)
                        .build();

                FirebaseMessaging.getInstance().send(msg);

            } catch (FirebaseMessagingException e) {
                throw new RuntimeException("Firebase send failed", e);
            }
        });
    }
}
package com.fokatindia.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount =
                    getClass().getClassLoader()
                            .getResourceAsStream("firebase-service-account.json");

            assert serviceAccount != null;
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("Firebase initialized successfully");


        } catch (Exception e) {
            throw new RuntimeException("Firebase init failed", e);
        }
    }
}
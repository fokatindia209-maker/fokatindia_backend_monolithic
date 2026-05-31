package com.fokatindia.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void init() {

        try {

            GoogleCredentials credentials;

            String firebaseConfig = System.getenv("FIREBASE_CONFIG");

            // Railway
            if (firebaseConfig != null && !firebaseConfig.isBlank()) {

                credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(
                                firebaseConfig.getBytes(StandardCharsets.UTF_8)
                        )
                );

                System.out.println("Firebase loaded from Railway ENV");
            }

            // Local
            else {

                InputStream serviceAccount =
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream("firebase-service-account.json");

                if (serviceAccount == null) {
                    throw new RuntimeException(
                            "firebase-service-account.json not found"
                    );
                }

                credentials =
                        GoogleCredentials.fromStream(serviceAccount);

                System.out.println("Firebase loaded from local file");
            }

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(credentials)
                            .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("Firebase initialized successfully");

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Firebase init failed",
                    e
            );
        }
    }
}
//package com.fokatindia.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//
//@Service
//public class FirebaseInitializer {
//
//    @PostConstruct
//    public void init() {
//        try {
//            InputStream serviceAccount =
//                    getClass().getClassLoader()
//                            .getResourceAsStream("firebase-service-account.json");
//
//            assert serviceAccount != null;
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//
//            System.out.println("Firebase initialized successfully");
//
//
//        } catch (Exception e) {
//            throw new RuntimeException("Firebase init failed", e);
//        }
//    }
//}
package com.fokatindia.dto.notification;

import lombok.Data;

@Data
public class NotificationRequest {

    private Long userId;

    private String title;

    private String message;

    private String fcmToken; // Firebase device token

    private String type;
}
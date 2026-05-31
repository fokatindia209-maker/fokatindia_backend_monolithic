package com.fokatindia.dto.notification;

import lombok.Data;

import java.util.List;

@Data
public class NotificationRequest {

    private Long userId;     // single user

    private List<Long> userIds;   // multiple users ⭐ NEW

    private String title;

    private Boolean sendToAll;  // NEW ⭐

    private String message;

    private String fcmToken; // Firebase device token

    private String type;
}
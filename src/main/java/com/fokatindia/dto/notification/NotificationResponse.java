package com.fokatindia.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String type;
    private Long referenceId;
    private Boolean readStatus;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
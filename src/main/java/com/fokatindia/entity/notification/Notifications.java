package com.fokatindia.entity.notification;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("notifications")
public class Notifications {

    @Id
    private Long id;

    private Long userId;

    private String title;

    private String message;

    private String type;
    // BOOKING, PAYMENT, SYSTEM, PROMO

    private Boolean readStatus = false;

    private LocalDateTime createdAt;
}
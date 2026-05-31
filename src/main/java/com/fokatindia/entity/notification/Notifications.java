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

    private String type; // BOOKING, PAYMENT, SYSTEM, PROMO

    private Long referenceId; // bookingId/paymentId etc (VERY useful)

    private Boolean readStatus = false;

    private Boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
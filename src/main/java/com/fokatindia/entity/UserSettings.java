package com.fokatindia.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

// SQL to create this table:
// CREATE TABLE IF NOT EXISTS user_settings (
//     id BIGSERIAL PRIMARY KEY,
//     user_id BIGINT UNIQUE NOT NULL,
//     email_notifications BOOLEAN DEFAULT TRUE,
//     sms_notifications BOOLEAN DEFAULT TRUE,
//     push_notifications BOOLEAN DEFAULT TRUE,
//     two_factor_auth BOOLEAN DEFAULT FALSE,
//     login_alerts BOOLEAN DEFAULT TRUE,
//     timezone VARCHAR(100) DEFAULT 'Asia/Kolkata',
//     language VARCHAR(50) DEFAULT 'English',
//     last_backup_at TIMESTAMP,
//     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
// );

@Data
@Table("user_settings")
public class UserSettings {

    @Id
    private Long id;

    private Long userId;

    private Boolean emailNotifications = true;

    private Boolean smsNotifications = true;

    private Boolean pushNotifications = true;

    private Boolean twoFactorAuth = false;

    private Boolean loginAlerts = true;

    private String timezone = "Asia/Kolkata";

    private String language = "English";

    private LocalDateTime lastBackupAt;

    private LocalDateTime updatedAt;
}

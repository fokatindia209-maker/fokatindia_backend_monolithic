package com.fokatindia.dto.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsResponse {
    private Long userId;
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
    private Boolean twoFactorAuth;
    private Boolean loginAlerts;
    private String timezone;
    private String language;
    private LocalDateTime lastBackupAt;
}

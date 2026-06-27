package com.fokatindia.dto.settings;

import lombok.Data;

@Data
public class UserSettingsRequest {
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
    private Boolean twoFactorAuth;
    private Boolean loginAlerts;
    private String timezone;
    private String language;
}

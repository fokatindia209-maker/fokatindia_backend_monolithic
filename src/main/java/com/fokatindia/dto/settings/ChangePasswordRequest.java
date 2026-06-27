package com.fokatindia.dto.settings;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Long userId;
    private String currentPassword;
    private String newPassword;
}

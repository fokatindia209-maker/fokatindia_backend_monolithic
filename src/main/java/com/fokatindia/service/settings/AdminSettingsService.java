package com.fokatindia.service.settings;

import com.fokatindia.dto.settings.ChangePasswordRequest;
import com.fokatindia.dto.settings.UserSettingsRequest;
import com.fokatindia.dto.settings.UserSettingsResponse;
import reactor.core.publisher.Mono;

public interface AdminSettingsService {
    Mono<UserSettingsResponse> getSettings(Long userId);
    Mono<UserSettingsResponse> updateSettings(Long userId, UserSettingsRequest request);
    Mono<String> changePassword(ChangePasswordRequest request);
    Mono<UserSettingsResponse> triggerBackup(Long userId);
}

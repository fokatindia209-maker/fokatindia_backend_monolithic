package com.fokatindia.service.impl.settings;

import com.fokatindia.dto.settings.ChangePasswordRequest;
import com.fokatindia.dto.settings.UserSettingsRequest;
import com.fokatindia.dto.settings.UserSettingsResponse;
import com.fokatindia.entity.UserSettings;
import com.fokatindia.repository.UserRepository;
import com.fokatindia.repository.UserSettingsRepository;
import com.fokatindia.service.settings.AdminSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSettingsServiceImpl implements AdminSettingsService {

    private final UserSettingsRepository settingsRepository;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // =====================================================
    // GET SETTINGS (create defaults if not found)
    // =====================================================

    @Override
    public Mono<UserSettingsResponse> getSettings(Long userId) {
        return settingsRepository.findByUserId(userId)
                .switchIfEmpty(createAndSaveDefaults(userId))
                .map(this::toResponse);
    }

    // =====================================================
    // UPDATE SETTINGS
    // =====================================================

    @Override
    public Mono<UserSettingsResponse> updateSettings(Long userId, UserSettingsRequest request) {
        return settingsRepository.findByUserId(userId)
                .switchIfEmpty(createAndSaveDefaults(userId))
                .flatMap(settings -> {
                    if (request.getEmailNotifications() != null)
                        settings.setEmailNotifications(request.getEmailNotifications());
                    if (request.getSmsNotifications() != null)
                        settings.setSmsNotifications(request.getSmsNotifications());
                    if (request.getPushNotifications() != null)
                        settings.setPushNotifications(request.getPushNotifications());
                    if (request.getTwoFactorAuth() != null)
                        settings.setTwoFactorAuth(request.getTwoFactorAuth());
                    if (request.getLoginAlerts() != null)
                        settings.setLoginAlerts(request.getLoginAlerts());
                    if (request.getTimezone() != null)
                        settings.setTimezone(request.getTimezone());
                    if (request.getLanguage() != null)
                        settings.setLanguage(request.getLanguage());
                    settings.setUpdatedAt(LocalDateTime.now());
                    return settingsRepository.save(settings);
                })
                .map(this::toResponse);
    }

    // =====================================================
    // CHANGE PASSWORD
    // =====================================================

    @Override
    public Mono<String> changePassword(ChangePasswordRequest request) {
        return userRepository.findById(request.getUserId())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                        return Mono.error(new RuntimeException("Current password is incorrect"));
                    }
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    return userRepository.save(user);
                })
                .thenReturn("Password changed successfully");
    }

    // =====================================================
    // TRIGGER BACKUP
    // =====================================================

    @Override
    public Mono<UserSettingsResponse> triggerBackup(Long userId) {
        return settingsRepository.findByUserId(userId)
                .switchIfEmpty(createAndSaveDefaults(userId))
                .flatMap(settings -> {
                    settings.setLastBackupAt(LocalDateTime.now());
                    settings.setUpdatedAt(LocalDateTime.now());
                    return settingsRepository.save(settings);
                })
                .map(this::toResponse);
    }

    // =====================================================
    // HELPERS
    // =====================================================

    private Mono<UserSettings> createAndSaveDefaults(Long userId) {
        UserSettings settings = new UserSettings();
        settings.setUserId(userId);
        settings.setEmailNotifications(true);
        settings.setSmsNotifications(true);
        settings.setPushNotifications(true);
        settings.setTwoFactorAuth(false);
        settings.setLoginAlerts(true);
        settings.setTimezone("Asia/Kolkata");
        settings.setLanguage("English");
        settings.setUpdatedAt(LocalDateTime.now());
        return settingsRepository.save(settings);
    }

    private UserSettingsResponse toResponse(UserSettings s) {
        return new UserSettingsResponse(
                s.getUserId(),
                s.getEmailNotifications(),
                s.getSmsNotifications(),
                s.getPushNotifications(),
                s.getTwoFactorAuth(),
                s.getLoginAlerts(),
                s.getTimezone(),
                s.getLanguage(),
                s.getLastBackupAt()
        );
    }
}

package com.fokatindia.controller.settings;

import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.settings.ChangePasswordRequest;
import com.fokatindia.dto.settings.UserSettingsRequest;
import com.fokatindia.dto.settings.UserSettingsResponse;
import com.fokatindia.service.settings.AdminSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/restful/v1/api/admin/settings")
@RequiredArgsConstructor
public class AdminSettingsController {

    private final AdminSettingsService service;

    // =====================================================
    // GET SETTINGS
    // =====================================================

    @GetMapping("/{userId}")
    public Mono<ApiResponse<UserSettingsResponse>> getSettings(
            @PathVariable Long userId
    ) {
        return service.getSettings(userId)
                .map(data -> new ApiResponse<>(
                        "success", 200, "Settings fetched successfully", data
                ));
    }

    // =====================================================
    // UPDATE SETTINGS
    // =====================================================

    @PutMapping("/{userId}")
    public Mono<ApiResponse<UserSettingsResponse>> updateSettings(
            @PathVariable Long userId,
            @RequestBody UserSettingsRequest request
    ) {
        return service.updateSettings(userId, request)
                .map(data -> new ApiResponse<>(
                        "success", 200, "Settings updated successfully", data
                ));
    }

    // =====================================================
    // CHANGE PASSWORD
    // =====================================================

    @PutMapping("/password")
    public Mono<ApiResponse<String>> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {
        return service.changePassword(request)
                .map(msg -> new ApiResponse<>(
                        "success", 200, msg, msg
                ))
                .onErrorResume(e -> Mono.just(new ApiResponse<>(
                        "error", 400, e.getMessage(), (String) null, null
                )));
    }

    // =====================================================
    // TRIGGER BACKUP
    // =====================================================

    @PostMapping("/backup/{userId}")
    public Mono<ApiResponse<UserSettingsResponse>> triggerBackup(
            @PathVariable Long userId
    ) {
        return service.triggerBackup(userId)
                .map(data -> new ApiResponse<>(
                        "success", 200, "Backup triggered successfully", data
                ));
    }
}

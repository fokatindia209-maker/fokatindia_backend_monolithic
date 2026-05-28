package com.fokatindia.controller.notification;


import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.notification.NotificationRequest;
import com.fokatindia.dto.notification.NotificationResponse;
import com.fokatindia.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/restful/v1/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PreAuthorize("hasAuthority('NOTIFICATION_SEND')")
    @PostMapping("")
    public Mono<ApiResponse<NotificationResponse>> send(
            @RequestBody NotificationRequest request
    ) {

        return service.sendNotification(request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Notification sent successfully",
                        res
                ));
    }

    // =========================================
    // GET ALL NOTIFICATIONS
    // =========================================
    @PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
    @GetMapping("")
    public Mono<ApiResponse<List<NotificationResponse>>> getAll() {
        return service.getAll()
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Notification list fetched successfully",
                        list
                ));
    }

    // =========================================
    // GET NOTIFICATION BY ID
    // =========================================
    @PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
    @GetMapping("/{id}")
    public Mono<ApiResponse<NotificationResponse>> getById(
            @PathVariable Long id
    ) {

        return service.getById(id)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Notification fetched successfully",
                        res
                ));
    }

    // =========================================
    // UPDATE NOTIFICATION
    // =========================================
    @PreAuthorize("hasAuthority('NOTIFICATION_UPDATE')")
    @PutMapping("/{id}")
    public Mono<ApiResponse<NotificationResponse>> update(
            @PathVariable Long id,
            @RequestBody NotificationRequest request
    ) {

        return service.update(id, request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Notification updated successfully",
                        res
                ));
    }

    // =========================================
    // MARK AS READ
    // =========================================
    @PreAuthorize("hasAuthority('NOTIFICATION_UPDATE')")
    @PutMapping("/{id}/read")
    public Mono<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable Long id
    ) {

        return service.markAsRead(id)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Notification marked as read",
                        res
                ));
    }

    // =========================================
    // DELETE NOTIFICATION
    // =========================================
    @PreAuthorize("hasAuthority('NOTIFICATION_DELETE')")
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> delete(
            @PathVariable Long id
    ) {

        return service.delete(id)
                .thenReturn(
                        new ApiResponse<>(
                                "success",
                                200,
                                "Notification deleted successfully",
                                "Deleted"
                        )
                );
    }
}

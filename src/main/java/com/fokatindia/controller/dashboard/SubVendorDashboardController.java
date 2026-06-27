package com.fokatindia.controller.dashboard;

import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.dashboard.subvendor.SubVendorDashboardResponse;
import com.fokatindia.service.dashboard.SubVendorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/restful/v1/api/subvendor/dashboard")
@RequiredArgsConstructor
public class SubVendorDashboardController {

    private final SubVendorDashboardService service;

    @PreAuthorize("hasAuthority('BOOKING_VIEW')")
    @GetMapping("/{subVendorId}")
    public Mono<ApiResponse<SubVendorDashboardResponse>> getDashboard(
            @PathVariable Long subVendorId
    ) {
        return service.getDashboardStats(subVendorId)
                .map(data -> new ApiResponse<>(
                        "success", 200, "SubVendor dashboard fetched successfully", data
                ));
    }
}

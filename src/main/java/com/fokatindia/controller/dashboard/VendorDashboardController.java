package com.fokatindia.controller.dashboard;

import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.dashboard.vendor.VendorDashboardResponse;
import com.fokatindia.service.dashboard.VendorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/restful/v1/api/vendor/dashboard")
@RequiredArgsConstructor
public class VendorDashboardController {

    private final VendorDashboardService service;

    @PreAuthorize("hasAuthority('BOOKING_VIEW')")
    @GetMapping("/{vendorId}")
    public Mono<ApiResponse<VendorDashboardResponse>> getDashboard(
            @PathVariable Long vendorId
    ) {
        return service.getDashboardStats(vendorId)
                .map(data -> new ApiResponse<>(
                        "success", 200, "Vendor dashboard fetched successfully", data
                ));
    }
}

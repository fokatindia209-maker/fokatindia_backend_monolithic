package com.fokatindia.controller.dashboard;

import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.dashboard.AdminDashboardResponse;
import com.fokatindia.service.dashboard.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/restful/v1/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService service;

    @GetMapping("/dashboard")
    public Mono<ApiResponse<AdminDashboardResponse>> getDashboard() {
        return service.getDashboardStats()
                .map(data -> new ApiResponse<>(
                        "success",
                        200,
                        "Dashboard data fetched successfully",
                        data
                ));
    }
}

package com.fokatindia.service.dashboard;

import com.fokatindia.dto.dashboard.AdminDashboardResponse;
import reactor.core.publisher.Mono;

public interface AdminDashboardService {
    Mono<AdminDashboardResponse> getDashboardStats();
}

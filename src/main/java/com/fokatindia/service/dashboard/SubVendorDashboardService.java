package com.fokatindia.service.dashboard;

import com.fokatindia.dto.dashboard.subvendor.SubVendorDashboardResponse;
import reactor.core.publisher.Mono;

public interface SubVendorDashboardService {
    Mono<SubVendorDashboardResponse> getDashboardStats(Long subVendorId);
}

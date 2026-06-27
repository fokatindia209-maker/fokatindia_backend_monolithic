package com.fokatindia.service.dashboard;

import com.fokatindia.dto.dashboard.vendor.VendorDashboardResponse;
import reactor.core.publisher.Mono;

public interface VendorDashboardService {
    Mono<VendorDashboardResponse> getDashboardStats(Long vendorId);
}

package com.fokatindia.service.impl.dashboard;

import com.fokatindia.dto.dashboard.AdminDashboardResponse;
import com.fokatindia.repository.dashboard.AdminDashboardRepository;
import com.fokatindia.service.dashboard.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final AdminDashboardRepository repository;

    @Override
    public Mono<AdminDashboardResponse> getDashboardStats() {
        return Mono.zip(
                repository.countAllUsers(),
                repository.countAllVendors(),
                repository.countAllBookings(),
                repository.sumTotalRevenue(),
                repository.countPendingKycVendors(),
                repository.countPendingBookings(),
                repository.findRecentBookings().collectList(),
                repository.findMonthlyBookingStats().collectList()
        ).flatMap(tuple ->
                repository.findTopVendors().collectList()
                        .map(topVendors -> AdminDashboardResponse.builder()
                                .totalUsers(tuple.getT1())
                                .totalVendors(tuple.getT2())
                                .totalBookings(tuple.getT3())
                                .totalRevenue(tuple.getT4())
                                .pendingKycVendors(tuple.getT5())
                                .pendingBookings(tuple.getT6())
                                .recentBookings(tuple.getT7())
                                .monthlyBookingStats(tuple.getT8())
                                .topVendors(topVendors)
                                .build()
                        )
        );
    }
}

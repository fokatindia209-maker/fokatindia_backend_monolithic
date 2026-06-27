package com.fokatindia.service.impl.dashboard;

import com.fokatindia.dto.dashboard.vendor.VendorDashboardResponse;
import com.fokatindia.repository.dashboard.VendorDashboardRepository;
import com.fokatindia.service.dashboard.VendorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VendorDashboardServiceImpl implements VendorDashboardService {

    private final VendorDashboardRepository repo;

    @Override
    public Mono<VendorDashboardResponse> getDashboardStats(Long vendorId) {
        return Mono.zip(
                repo.countTotalBookings(vendorId),
                repo.sumTotalEarnings(vendorId),
                repo.countSubVendors(vendorId),
                repo.countActiveJobs(vendorId),
                repo.countCompletedJobs(vendorId),
                repo.countCancelledJobs(vendorId),
                repo.findAvgRating(vendorId),
                repo.findMonthlyBookingStats(vendorId).collectList()
        ).flatMap(t ->
                repo.findRecentBookings(vendorId).collectList().flatMap(recentBookings ->
                        repo.findTopSubVendors(vendorId).collectList().flatMap(topSubVendors ->
                                repo.findRecentReviews(vendorId).collectList().map(recentReviews ->
                                        VendorDashboardResponse.builder()
                                                .totalBookings(t.getT1())
                                                .totalEarnings(t.getT2())
                                                .totalSubVendors(t.getT3())
                                                .activeJobs(t.getT4())
                                                .completedJobs(t.getT5())
                                                .cancelledJobs(t.getT6())
                                                .averageRating(t.getT7())
                                                .monthlyBookingStats(t.getT8())
                                                .recentBookings(recentBookings)
                                                .topSubVendors(topSubVendors)
                                                .recentReviews(recentReviews)
                                                .build()
                                )
                        )
                )
        );
    }
}

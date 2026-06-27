package com.fokatindia.service.impl.dashboard;

import com.fokatindia.dto.dashboard.subvendor.SubVendorDashboardResponse;
import com.fokatindia.repository.dashboard.SubVendorDashboardRepository;
import com.fokatindia.service.dashboard.SubVendorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SubVendorDashboardServiceImpl implements SubVendorDashboardService {

    private final SubVendorDashboardRepository repo;

    @Override
    public Mono<SubVendorDashboardResponse> getDashboardStats(Long subVendorId) {
        return Mono.zip(
                repo.countTotalBookings(subVendorId),
                repo.sumTotalEarnings(subVendorId),
                repo.countActiveJobs(subVendorId),
                repo.countCompletedJobs(subVendorId),
                repo.countCancelledJobs(subVendorId),
                repo.findAvgRating(subVendorId),
                repo.countTotalReviews(subVendorId),
                repo.findMonthlyBookingStats(subVendorId).collectList()
        ).flatMap(t ->
                repo.findRecentBookings(subVendorId).collectList().flatMap(recentBookings ->
                        repo.findRecentReviews(subVendorId).collectList().map(recentReviews ->
                                SubVendorDashboardResponse.builder()
                                        .totalBookings(t.getT1())
                                        .totalEarnings(t.getT2())
                                        .activeJobs(t.getT3())
                                        .completedJobs(t.getT4())
                                        .cancelledJobs(t.getT5())
                                        .averageRating(t.getT6())
                                        .totalReviews(t.getT7())
                                        .monthlyBookingStats(t.getT8())
                                        .recentBookings(recentBookings)
                                        .recentReviews(recentReviews)
                                        .build()
                        )
                )
        );
    }
}

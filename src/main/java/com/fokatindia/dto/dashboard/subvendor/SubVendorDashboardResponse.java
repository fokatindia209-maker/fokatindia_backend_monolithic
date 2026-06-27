package com.fokatindia.dto.dashboard.subvendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubVendorDashboardResponse {
    private Long totalBookings;
    private Double totalEarnings;
    private Long activeJobs;
    private Long completedJobs;
    private Long cancelledJobs;
    private Double averageRating;
    private Long totalReviews;
    private List<SubVendorMonthlyBookingStat> monthlyBookingStats;
    private List<SubVendorRecentBookingItem> recentBookings;
    private List<SubVendorRecentReviewItem> recentReviews;
}

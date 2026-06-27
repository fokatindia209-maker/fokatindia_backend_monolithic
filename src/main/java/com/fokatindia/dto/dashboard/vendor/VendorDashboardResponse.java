package com.fokatindia.dto.dashboard.vendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDashboardResponse {
    private Long totalBookings;
    private Double totalEarnings;
    private Long totalSubVendors;
    private Long activeJobs;
    private Long completedJobs;
    private Long cancelledJobs;
    private Double averageRating;
    private List<VendorMonthlyBookingStat> monthlyBookingStats;
    private List<VendorRecentBookingItem> recentBookings;
    private List<VendorTopSubVendorItem> topSubVendors;
    private List<VendorRecentReviewItem> recentReviews;
}

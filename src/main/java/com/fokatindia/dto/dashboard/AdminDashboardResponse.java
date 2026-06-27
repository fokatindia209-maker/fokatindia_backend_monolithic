package com.fokatindia.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponse {
    private Long totalUsers;
    private Long totalVendors;
    private Long totalBookings;
    private Double totalRevenue;
    private Long pendingKycVendors;
    private Long pendingBookings;
    private List<MonthlyBookingStat> monthlyBookingStats;
    private List<RecentBookingItem> recentBookings;
    private List<TopVendorItem> topVendors;
}

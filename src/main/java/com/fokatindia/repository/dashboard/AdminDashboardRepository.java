package com.fokatindia.repository.dashboard;

import com.fokatindia.dto.dashboard.MonthlyBookingStat;
import com.fokatindia.dto.dashboard.RecentBookingItem;
import com.fokatindia.dto.dashboard.TopVendorItem;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class AdminDashboardRepository {

    private final DatabaseClient db;

    public Mono<Long> countAllUsers() {
        return db.sql("SELECT COUNT(*) FROM users")
                .map(row -> row.get(0, Long.class))
                .one();
    }

    public Mono<Long> countAllVendors() {
        return db.sql("SELECT COUNT(*) FROM vendors")
                .map(row -> row.get(0, Long.class))
                .one();
    }

    public Mono<Long> countAllBookings() {
        return db.sql("SELECT COUNT(*) FROM bookings")
                .map(row -> row.get(0, Long.class))
                .one();
    }

    public Mono<Double> sumTotalRevenue() {
        return db.sql("SELECT COALESCE(SUM(final_amount), 0) FROM bookings WHERE payment_status = 'PAID'")
                .map(row -> {
                    Object val = row.get(0);
                    if (val instanceof Number n) return n.doubleValue();
                    return 0.0;
                })
                .one();
    }

    public Mono<Long> countPendingKycVendors() {
        return db.sql("SELECT COUNT(*) FROM vendors WHERE kyc_status = 'PENDING'")
                .map(row -> row.get(0, Long.class))
                .one();
    }

    public Mono<Long> countPendingBookings() {
        return db.sql("SELECT COUNT(*) FROM bookings WHERE booking_status = 'PENDING'")
                .map(row -> row.get(0, Long.class))
                .one();
    }

    public Flux<RecentBookingItem> findRecentBookings() {
        String sql = """
                SELECT b.id,
                       b.booking_code,
                       COALESCE(u.name, 'Unknown') AS user_name,
                       b.booking_status,
                       b.payment_status,
                       COALESCE(b.final_amount, 0.0) AS final_amount,
                       b.booking_date
                FROM bookings b
                LEFT JOIN users u ON b.user_id = u.user_id
                ORDER BY b.created_at DESC
                LIMIT 5
                """;

        return db.sql(sql)
                .map(row -> new RecentBookingItem(
                        row.get("id", Long.class),
                        row.get("booking_code", String.class),
                        row.get("user_name", String.class),
                        row.get("booking_status", String.class),
                        row.get("payment_status", String.class),
                        row.get("final_amount", Double.class),
                        row.get("booking_date", LocalDate.class)
                ))
                .all();
    }

    public Flux<MonthlyBookingStat> findMonthlyBookingStats() {
        String sql = """
                SELECT TO_CHAR(DATE_TRUNC('month', booking_date), 'Mon YYYY') AS month,
                       COUNT(*) AS count
                FROM bookings
                WHERE booking_date >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '5 months'
                GROUP BY DATE_TRUNC('month', booking_date)
                ORDER BY DATE_TRUNC('month', booking_date)
                """;

        return db.sql(sql)
                .map(row -> new MonthlyBookingStat(
                        row.get("month", String.class),
                        row.get("count", Long.class)
                ))
                .all();
    }

    public Flux<TopVendorItem> findTopVendors() {
        String sql = """
                SELECT vendor_id,
                       business_name,
                       COALESCE(rating, 0.0) AS rating,
                       kyc_status
                FROM vendors
                ORDER BY rating DESC NULLS LAST
                LIMIT 5
                """;

        return db.sql(sql)
                .map(row -> new TopVendorItem(
                        row.get("vendor_id", Long.class),
                        row.get("business_name", String.class),
                        row.get("rating", Double.class),
                        row.get("kyc_status", String.class)
                ))
                .all();
    }
}

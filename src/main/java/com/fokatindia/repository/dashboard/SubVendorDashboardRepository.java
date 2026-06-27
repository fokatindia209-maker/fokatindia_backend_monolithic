package com.fokatindia.repository.dashboard;

import com.fokatindia.dto.dashboard.subvendor.SubVendorMonthlyBookingStat;
import com.fokatindia.dto.dashboard.subvendor.SubVendorRecentBookingItem;
import com.fokatindia.dto.dashboard.subvendor.SubVendorRecentReviewItem;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SubVendorDashboardRepository {

    private final DatabaseClient db;

    public Mono<Long> countTotalBookings(Long subVendorId) {
        return db.sql("SELECT COUNT(*) FROM bookings WHERE sub_vendor_id = :subVendorId")
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Double> sumTotalEarnings(Long subVendorId) {
        return db.sql("""
                    SELECT COALESCE(SUM(sub_vendor_amount), 0.0)
                    FROM bookings
                    WHERE sub_vendor_id = :subVendorId
                      AND booking_status = 'SUCCESS'
                      AND payment_status = 'SUCCESS'
                    """)
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> {
                    Object val = row.get(0);
                    if (val == null) return 0.0;
                    return ((Number) val).doubleValue();
                })
                .one()
                .defaultIfEmpty(0.0);
    }

    public Mono<Long> countActiveJobs(Long subVendorId) {
        return db.sql("""
                    SELECT COUNT(*) FROM bookings
                    WHERE sub_vendor_id = :subVendorId
                      AND booking_status NOT IN ('SUCCESS', 'CANCELLED')
                    """)
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Long> countCompletedJobs(Long subVendorId) {
        return db.sql("SELECT COUNT(*) FROM bookings WHERE sub_vendor_id = :subVendorId AND booking_status = 'SUCCESS'")
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Long> countCancelledJobs(Long subVendorId) {
        return db.sql("SELECT COUNT(*) FROM bookings WHERE sub_vendor_id = :subVendorId AND booking_status = 'CANCELLED'")
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Double> findAvgRating(Long subVendorId) {
        return db.sql("""
                    SELECT COALESCE(AVG(rating), 0.0)
                    FROM reviews
                    WHERE sub_vendor_id = :subVendorId AND active = true
                    """)
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> {
                    Object val = row.get(0);
                    if (val == null) return 0.0;
                    return ((Number) val).doubleValue();
                })
                .one()
                .defaultIfEmpty(0.0);
    }

    public Mono<Long> countTotalReviews(Long subVendorId) {
        return db.sql("SELECT COUNT(*) FROM reviews WHERE sub_vendor_id = :subVendorId AND active = true")
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Flux<SubVendorMonthlyBookingStat> findMonthlyBookingStats(Long subVendorId) {
        return db.sql("""
                    SELECT TO_CHAR(DATE_TRUNC('month', booking_date), 'Mon YYYY') AS month,
                           COUNT(*) AS count
                    FROM bookings
                    WHERE sub_vendor_id = :subVendorId
                      AND booking_date >= CURRENT_DATE - INTERVAL '6 months'
                    GROUP BY DATE_TRUNC('month', booking_date)
                    ORDER BY DATE_TRUNC('month', booking_date)
                    """)
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> new SubVendorMonthlyBookingStat(
                        row.get("month", String.class),
                        row.get("count", Long.class)
                ))
                .all();
    }

    public Flux<SubVendorRecentBookingItem> findRecentBookings(Long subVendorId) {
        return db.sql("""
                    SELECT b.id, b.booking_code, u.name AS user_name,
                           b.booking_status, b.payment_status,
                           b.final_amount, b.sub_vendor_amount, b.booking_date
                    FROM bookings b
                    LEFT JOIN users u ON b.user_id = u.user_id
                    WHERE b.sub_vendor_id = :subVendorId
                    ORDER BY b.created_at DESC
                    LIMIT 5
                    """)
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> new SubVendorRecentBookingItem(
                        row.get("id", Long.class),
                        row.get("booking_code", String.class),
                        row.get("user_name", String.class),
                        row.get("booking_status", String.class),
                        row.get("payment_status", String.class),
                        row.get("final_amount", Double.class),
                        row.get("sub_vendor_amount", Double.class),
                        row.get("booking_date", java.time.LocalDate.class)
                ))
                .all();
    }

    public Flux<SubVendorRecentReviewItem> findRecentReviews(Long subVendorId) {
        return db.sql("""
                    SELECT r.id, u.name AS user_name, r.rating, r.comment, r.created_at
                    FROM reviews r
                    LEFT JOIN users u ON r.user_id = u.user_id
                    WHERE r.sub_vendor_id = :subVendorId AND r.active = true
                    ORDER BY r.created_at DESC
                    LIMIT 5
                    """)
                .bind("subVendorId", subVendorId)
                .map((row, meta) -> new SubVendorRecentReviewItem(
                        row.get("id", Long.class),
                        row.get("user_name", String.class),
                        row.get("rating", Integer.class),
                        row.get("comment", String.class),
                        row.get("created_at", java.time.LocalDateTime.class)
                ))
                .all();
    }
}

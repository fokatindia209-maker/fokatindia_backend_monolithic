package com.fokatindia.repository.dashboard;

import com.fokatindia.dto.dashboard.vendor.VendorMonthlyBookingStat;
import com.fokatindia.dto.dashboard.vendor.VendorRecentBookingItem;
import com.fokatindia.dto.dashboard.vendor.VendorRecentReviewItem;
import com.fokatindia.dto.dashboard.vendor.VendorTopSubVendorItem;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class VendorDashboardRepository {

    private final DatabaseClient db;

    public Mono<Long> countTotalBookings(Long vendorId) {
        return db.sql("SELECT COUNT(*) FROM bookings WHERE vendor_id = :vendorId")
                .bind("vendorId", vendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Double> sumTotalEarnings(Long vendorId) {
        return db.sql("""
                    SELECT COALESCE(SUM(vendor_amount), 0.0)
                    FROM bookings
                    WHERE vendor_id = :vendorId
                      AND booking_status = 'SUCCESS'
                      AND payment_status = 'SUCCESS'
                    """)
                .bind("vendorId", vendorId)
                .map((row, meta) -> {
                    Object val = row.get(0);
                    if (val == null) return 0.0;
                    return ((Number) val).doubleValue();
                })
                .one()
                .defaultIfEmpty(0.0);
    }

    public Mono<Long> countSubVendors(Long vendorId) {
        return db.sql("SELECT COUNT(*) FROM sub_vendors WHERE vendor_id = :vendorId")
                .bind("vendorId", vendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Long> countActiveJobs(Long vendorId) {
        return db.sql("""
                    SELECT COUNT(*) FROM bookings
                    WHERE vendor_id = :vendorId
                      AND booking_status NOT IN ('SUCCESS', 'CANCELLED')
                    """)
                .bind("vendorId", vendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Long> countCompletedJobs(Long vendorId) {
        return db.sql("SELECT COUNT(*) FROM bookings WHERE vendor_id = :vendorId AND booking_status = 'SUCCESS'")
                .bind("vendorId", vendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Long> countCancelledJobs(Long vendorId) {
        return db.sql("SELECT COUNT(*) FROM bookings WHERE vendor_id = :vendorId AND booking_status = 'CANCELLED'")
                .bind("vendorId", vendorId)
                .map((row, meta) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    public Mono<Double> findAvgRating(Long vendorId) {
        return db.sql("""
                    SELECT COALESCE(AVG(rating), 0.0)
                    FROM reviews
                    WHERE vendor_id = :vendorId AND active = true
                    """)
                .bind("vendorId", vendorId)
                .map((row, meta) -> {
                    Object val = row.get(0);
                    if (val == null) return 0.0;
                    return ((Number) val).doubleValue();
                })
                .one()
                .defaultIfEmpty(0.0);
    }

    public Flux<VendorMonthlyBookingStat> findMonthlyBookingStats(Long vendorId) {
        return db.sql("""
                    SELECT TO_CHAR(DATE_TRUNC('month', booking_date), 'Mon YYYY') AS month,
                           COUNT(*) AS count
                    FROM bookings
                    WHERE vendor_id = :vendorId
                      AND booking_date >= CURRENT_DATE - INTERVAL '6 months'
                    GROUP BY DATE_TRUNC('month', booking_date)
                    ORDER BY DATE_TRUNC('month', booking_date)
                    """)
                .bind("vendorId", vendorId)
                .map((row, meta) -> new VendorMonthlyBookingStat(
                        row.get("month", String.class),
                        row.get("count", Long.class)
                ))
                .all();
    }

    public Flux<VendorRecentBookingItem> findRecentBookings(Long vendorId) {
        return db.sql("""
                    SELECT b.id, b.booking_code, u.name AS user_name,
                           b.booking_status, b.payment_status,
                           b.final_amount, b.booking_date
                    FROM bookings b
                    LEFT JOIN users u ON b.user_id = u.user_id
                    WHERE b.vendor_id = :vendorId
                    ORDER BY b.created_at DESC
                    LIMIT 5
                    """)
                .bind("vendorId", vendorId)
                .map((row, meta) -> new VendorRecentBookingItem(
                        row.get("id", Long.class),
                        row.get("booking_code", String.class),
                        row.get("user_name", String.class),
                        row.get("booking_status", String.class),
                        row.get("payment_status", String.class),
                        row.get("final_amount", Double.class),
                        row.get("booking_date", java.time.LocalDate.class)
                ))
                .all();
    }

    public Flux<VendorTopSubVendorItem> findTopSubVendors(Long vendorId) {
        return db.sql("""
                    SELECT sv.sub_vendor_id, u.name,
                           sv.specialization, sv.availability_status, sv.rating,
                           COUNT(b.id) AS total_jobs
                    FROM sub_vendors sv
                    LEFT JOIN users u ON sv.user_id = u.user_id
                    LEFT JOIN bookings b ON b.sub_vendor_id = sv.sub_vendor_id
                    WHERE sv.vendor_id = :vendorId
                    GROUP BY sv.sub_vendor_id, u.name, sv.specialization, sv.availability_status, sv.rating
                    ORDER BY total_jobs DESC
                    LIMIT 5
                    """)
                .bind("vendorId", vendorId)
                .map((row, meta) -> new VendorTopSubVendorItem(
                        row.get("sub_vendor_id", Long.class),
                        row.get("name", String.class),
                        row.get("specialization", String.class),
                        row.get("availability_status", String.class),
                        row.get("rating", Double.class),
                        row.get("total_jobs", Long.class)
                ))
                .all();
    }

    public Flux<VendorRecentReviewItem> findRecentReviews(Long vendorId) {
        return db.sql("""
                    SELECT r.id, u.name AS user_name, r.rating, r.comment, r.created_at
                    FROM reviews r
                    LEFT JOIN users u ON r.user_id = u.user_id
                    WHERE r.vendor_id = :vendorId AND r.active = true
                    ORDER BY r.created_at DESC
                    LIMIT 5
                    """)
                .bind("vendorId", vendorId)
                .map((row, meta) -> new VendorRecentReviewItem(
                        row.get("id", Long.class),
                        row.get("user_name", String.class),
                        row.get("rating", Integer.class),
                        row.get("comment", String.class),
                        row.get("created_at", java.time.LocalDateTime.class)
                ))
                .all();
    }
}

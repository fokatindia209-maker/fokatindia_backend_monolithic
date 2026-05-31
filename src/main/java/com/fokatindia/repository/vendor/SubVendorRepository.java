package com.fokatindia.repository.vendor;

import com.fokatindia.dto.vendor.SubVendorResponse;
import com.fokatindia.entity.vendor.SubVendor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SubVendorRepository  extends ReactiveCrudRepository<SubVendor, Long> {


    Flux<SubVendor> findByVendorId(Long vendorId);

    Flux<SubVendor> findByUserId(Long userId);

    @Query("""
SELECT
    sv.user_id AS user_id,
    sv.sub_vendor_id AS sub_vendor_id,
    sv.vendor_id AS vendor_id,
    sv.specialization AS specialization,
    sv.experience_years AS experience_years,
    sv.availability_status AS availability_status,
    sv.rating AS rating,
    sv.created_at AS created_at,
    u.name AS name,
    u.email AS email,
    u.phone AS phone,
    u.status AS status

FROM sub_vendors sv
JOIN users u ON sv.user_id = u.user_id
WHERE sv.vendor_id = :vendorId
""")
    Flux<SubVendorResponse> findSubVendorsWithUser(Long vendorId);
}
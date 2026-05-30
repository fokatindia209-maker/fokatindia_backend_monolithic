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
    sv.sub_vendor_id AS subVendorId,
    sv.vendor_id AS vendorId,
    sv.specialization AS specialization,
    sv.experience_years AS experienceYears,
    sv.availability_status AS availabilityStatus,
    sv.rating AS rating,
    
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
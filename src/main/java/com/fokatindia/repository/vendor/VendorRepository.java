package com.fokatindia.repository.vendor;

import com.fokatindia.dto.vendor.VendorResponse;
import com.fokatindia.entity.vendor.Vendor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VendorRepository extends ReactiveCrudRepository<Vendor, Long> {
    Mono<Vendor> findByUserId(Long userId);
    Flux<Vendor> findByVendorId(Long vendorId);
    @Query("""
SELECT
      v.vendor_id AS vendor_id,
                    v.user_id AS user_id,
                    v.business_name AS business_name,
                    v.gst_number AS gst_number,
                    v.address AS address,
                    v.city AS city,
                    v.service_area AS service_area,
                    v.kyc_status AS kyc_status,
                    v.rating AS rating,
                    u.name AS name,
                    u.email AS email,
                    u.phone AS phone,
                    u.status AS status
FROM vendors v
JOIN users u ON v.user_id = u.user_id
""")
    Flux<VendorResponse> findAllVendorDetailWithUserId();
}
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
    @Query("""
SELECT
    v.vendor_id,
    v.user_id,
    v.business_name,
    u.status,
    u.name,
    u.email,
    u.phone
FROM vendors v
JOIN users u ON v.user_id = u.user_id
""")
    Flux<VendorResponse> findAllVendorDetails();
}
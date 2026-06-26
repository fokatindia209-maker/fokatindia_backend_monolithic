package com.fokatindia.repository.vendor;

import com.fokatindia.entity.vendor.Category;
import com.fokatindia.entity.vendor.Service;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ServiceRepository
        extends ReactiveCrudRepository<Service, Long> {

    Flux<Service> findByCategoryId(Long categoryId);

    Flux<Service> findByActiveTrue();

    Flux<Service> findByFeaturedTrue();


    @Query("""
    SELECT s.*
    FROM service_categories s
    INNER JOIN vendor_services vs
        ON vs.service_id = s.id
    WHERE vs.vendor_id = :vendorId
      AND vs.active = true
      AND s.active = true
""")
    Flux<Service> findByVendorId(Long vendorId);


    @Query("""
    SELECT s.*
    FROM service_categories s
    INNER JOIN sub_vendor_services svs
        ON svs.service_id = s.id
    WHERE svs.sub_vendor_id = :subVendorId
      AND vs.active = true
      AND s.active = true
""")
    Flux<Service> findBySubVendorId(Long subVendorId);
}
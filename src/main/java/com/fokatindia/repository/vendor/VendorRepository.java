package com.fokatindia.repository.vendor;

import com.fokatindia.entity.vendor.Vendor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface VendorRepository extends ReactiveCrudRepository<Vendor, Long> {
    Mono<Vendor> findByUserId(Long userId);
}
package com.fokatindia.repository.vendor;

import com.fokatindia.entity.vendor.SubVendor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SubVendorRepository  extends ReactiveCrudRepository<SubVendor, Long> {


    Flux<SubVendor> findByVendorId(Long vendorId);

    Flux<SubVendor> findByUserId(Long userId);
}
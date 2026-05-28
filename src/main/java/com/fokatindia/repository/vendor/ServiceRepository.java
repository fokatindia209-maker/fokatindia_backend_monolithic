package com.fokatindia.repository.vendor;

import com.fokatindia.entity.vendor.Service;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ServiceRepository
        extends ReactiveCrudRepository<Service, Long> {

    Flux<Service> findByCategoryId(Long categoryId);

    Flux<Service> findByActiveTrue();

    Flux<Service> findByFeaturedTrue();
}
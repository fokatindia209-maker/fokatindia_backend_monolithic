package com.fokatindia.repository.review;

import com.fokatindia.entity.review.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReviewRepository extends ReactiveCrudRepository<Review, Long> {

    Flux<Review> findByVendorId(Long vendorId);

    Flux<Review> findByServiceId(Long serviceId);

    Flux<Review> findByUserId(Long userId);
}
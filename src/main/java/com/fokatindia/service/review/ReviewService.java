package com.fokatindia.service.review;

import com.fokatindia.dto.review.ReviewRequest;
import com.fokatindia.dto.review.ReviewResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {

    Mono<ReviewResponse> createReview(ReviewRequest request);

    Flux<ReviewResponse> getAll();

    Mono<ReviewResponse> getById(Long id);

    Mono<ReviewResponse> updateReview(Long id, ReviewRequest request);

    Mono<Void> deleteReview(Long id);

    Flux<ReviewResponse> getByVendorId(Long vendorId);

    Flux<ReviewResponse> getByServiceId(Long serviceId);
}
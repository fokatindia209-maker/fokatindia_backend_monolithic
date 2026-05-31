package com.fokatindia.service.review;

import com.fokatindia.dto.review.ReviewRequest;
import com.fokatindia.dto.review.ReviewResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {

    Mono<ReviewResponse> createReview(ReviewRequest request);

    Flux<ReviewResponse> getAllReviews();

    Mono<ReviewResponse> getReviewById(Long id);

    Flux<ReviewResponse> getReviewsByVendor(Long vendorId);

    Flux<ReviewResponse> getReviewsByService(Long serviceId);

    Flux<ReviewResponse> getReviewsBySubVendor(Long subVendorId);

    Flux<ReviewResponse> getReviewsByUser(Long userId);

    Mono<ReviewResponse> updateReview(Long id, ReviewRequest request);

    Mono<Void> deleteReview(Long id);

    Flux<ReviewResponse> getReviewsByBooking(Long bookingId);
    Flux<ReviewResponse> getReviewsByCategory(Long categoryId);

}
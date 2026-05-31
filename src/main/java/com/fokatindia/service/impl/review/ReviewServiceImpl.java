package com.fokatindia.service.impl.review;


import com.fokatindia.dto.review.ReviewRequest;
import com.fokatindia.dto.review.ReviewResponse;
import com.fokatindia.entity.review.Review;
import com.fokatindia.repository.review.ReviewRepository;
import com.fokatindia.service.review.ReviewService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    // ================= CREATE =================
    @Override
    public Mono<ReviewResponse> createReview(ReviewRequest request) {

        Review review = new Review();

        review.setBookingId(request.getBookingId());
        review.setUserId(request.getUserId());
        review.setVendorId(request.getVendorId());
        review.setSubVendorId(request.getSubVendorId());
        review.setCategoryId(request.getCategoryId());
        review.setServiceId(request.getServiceId());

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        review.setActive(true);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return repository.save(review)
                .map(this::mapToResponse);
    }

    // ================= GET ALL =================
    @Override
    public Flux<ReviewResponse> getAllReviews() {
        return repository.findAll()
                .map(this::mapToResponse);
    }

    // ================= GET BY ID =================
    @Override
    public Mono<ReviewResponse> getReviewById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse);
    }

    // ================= UPDATE =================
    @Override
    public Mono<ReviewResponse> updateReview(Long id, ReviewRequest request) {

        return repository.findById(id)
                .flatMap(existing -> {



                    if(request.getRating() != null){

                        existing.setRating(request.getRating());
                    }

                    if(request.getComment() != null){

                        existing.setComment(request.getComment());
                    }


                    existing.setUpdatedAt(LocalDateTime.now());

                    return repository.save(existing);
                })
                .map(this::mapToResponse);
    }

    // ================= DELETE =================
    @Override
    public Mono<Void> deleteReview(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<ReviewResponse> getReviewsByBooking(Long bookingId) {
        return repository.findByBookingId(bookingId)
                .map(this::mapToResponse);
    }

    // ================= FILTER BY VENDOR =================
    @Override
    public Flux<ReviewResponse> getReviewsByVendor(Long vendorId) {
        return repository.findByVendorId(vendorId)
                .map(this::mapToResponse);
    }

    @Override
    public Flux<ReviewResponse> getReviewsBySubVendor(Long subVendorId) {
        return repository.findBySubVendorId(subVendorId)
                .map(this::mapToResponse);
    }

    @Override
    public Flux<ReviewResponse> getReviewsByUser(Long userId) {
        return repository.findByUserId(userId)
                .map(this::mapToResponse);
    }

    // ================= FILTER BY SERVICE =================
    @Override
    public Flux<ReviewResponse> getReviewsByService(Long serviceId) {
        return repository.findByServiceId(serviceId)
                .map(this::mapToResponse);
    }

    // ================= FILTER BY SERVICE =================
    @Override
    public Flux<ReviewResponse> getReviewsByCategory(Long categoryId) {
        return repository.findByCategoryId(categoryId)
                .map(this::mapToResponse);
    }


    // ================= MAPPER =================
    private ReviewResponse mapToResponse(Review r) {
        ReviewResponse res = new ReviewResponse();

        res.setId(r.getId());
        res.setBookingId(r.getBookingId());
        res.setUserId(r.getUserId());
        res.setVendorId(r.getVendorId());
        res.setSubVendorId(r.getSubVendorId());
        res.setCategoryId(r.getCategoryId());
        res.setServiceId(r.getServiceId());
        res.setRating(r.getRating());
        res.setComment(r.getComment());
        res.setActive(r.getActive());
        res.setCreatedAt(r.getCreatedAt());
        res.setUpdatedAt(r.getUpdatedAt());

        return res;
    }
}
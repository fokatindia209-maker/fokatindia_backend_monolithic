package com.fokatindia.controller.review;


import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.review.ReviewRequest;
import com.fokatindia.dto.review.ReviewResponse;
import com.fokatindia.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ================= CREATE REVIEW =================
    @PreAuthorize("hasAnyAuthority('REVIEW_CREATE')")
    @PostMapping
    public Mono<ApiResponse<ReviewResponse>> createReview(
            @RequestBody ReviewRequest request
    ) {
        return reviewService.createReview(request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Review created successfully",
                        res
                ));
    }

    // ================= GET ALL REVIEWS =================


    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("")
    public Mono<ApiResponse<List<ReviewResponse>>> getAllReviews() {
        return reviewService.getAllReviews()
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Reviews fetched successfully",
                        list
                ));
    }


    // ================= GET BY ID =================
    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("/{id}")
    public Mono<ApiResponse<ReviewResponse>> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Review fetched successfully",
                        res
                ));
    }

    // ================= UPDATE REVIEW =================
    @PreAuthorize("hasAuthority('REVIEW_UPDATE')")
    @PutMapping("/{id}")
    public Mono<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequest request
    ) {
        return reviewService.updateReview(id, request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Review updated successfully",
                        res
                ));
    }

    // ================= DELETE REVIEW =================
    @PreAuthorize("hasAuthority('REVIEW_DELETE')")
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id)
                .thenReturn(
                        new ApiResponse<>(
                                "success",
                                200,
                                "Review deleted successfully",
                                "Deleted ID: " + id
                        )
                );
    }


    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("/vendor/{vendorId}")
    public Mono<ApiResponse<List<ReviewResponse>>> getReviewsByVendor(
            @PathVariable Long vendorId
    ) {
        return reviewService.getReviewsByVendor(vendorId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Vendor reviews fetched successfully",
                        list
                ));
    }

    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("/subvendor/{subVendorId}")
    public Mono<ApiResponse<List<ReviewResponse>>> getReviewsBySubVendor(
            @PathVariable Long subVendorId
    ) {
        return reviewService.getReviewsBySubVendor(subVendorId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "SubVendor reviews fetched successfully",
                        list
                ));
    }

    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("/user/{userId}")
    public Mono<ApiResponse<List<ReviewResponse>>> getReviewsByUser(
            @PathVariable Long userId
    ) {
        return reviewService.getReviewsByUser(userId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "User reviews fetched successfully",
                        list
                ));
    }

    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("/booking/{bookingId}")
    public Mono<ApiResponse<List<ReviewResponse>>> getReviewsByBooking(
            @PathVariable Long bookingId
    ) {
        return reviewService.getReviewsByBooking(bookingId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Booking reviews fetched successfully",
                        list
                ));
    }

    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("/service/{serviceId}")
    public Mono<ApiResponse<List<ReviewResponse>>> getReviewsByService(
            @PathVariable Long serviceId
    ) {
        return reviewService.getReviewsByService(serviceId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Service reviews fetched successfully",
                        list
                ));
    }



    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping("/category/{categoryId}")
    public Mono<ApiResponse<List<ReviewResponse>>> getReviewsByCategory(
            @PathVariable Long categoryId
    ) {
        return reviewService.getReviewsByCategory(categoryId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Category reviews fetched successfully",
                        list
                ));
    }
}
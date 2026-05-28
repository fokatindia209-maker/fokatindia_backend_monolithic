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
    public Mono<ApiResponse<List<ReviewResponse>>> getAll() {
        return reviewService.getAll()
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
    public Mono<ApiResponse<ReviewResponse>> getById(@PathVariable Long id) {
        return reviewService.getById(id)
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
}
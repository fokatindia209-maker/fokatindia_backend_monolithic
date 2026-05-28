package com.fokatindia.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {

    private Long id;

    private Long bookingId;

    private Long userId;

    private Long vendorId;

    private Long serviceId;

    private Integer rating;

    private String comment;

    private Boolean active;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
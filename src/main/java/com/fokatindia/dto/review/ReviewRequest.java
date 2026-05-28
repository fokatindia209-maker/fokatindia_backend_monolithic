package com.fokatindia.dto.review;

import lombok.Data;

@Data
public class ReviewRequest {

    private Long bookingId;

    private Long userId;

    private Long vendorId;

    private Long serviceId;

    private Integer rating;

    private String comment;
}
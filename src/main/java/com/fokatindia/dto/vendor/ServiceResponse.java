package com.fokatindia.dto.vendor;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ServiceResponse {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private String name;

    private String description;

    private Double price;

    private Double discountedPrice;

    private Double taxPercentage;

    private Double taxAmount;

    private Double finalPrice;

    private Integer durationMinutes;

    private String imageUrl;

    private Boolean featured;

    private Boolean active;

    private Double rating;

    private Integer totalBookings;

    private String serviceType;

    private LocalDateTime createdAt;
}
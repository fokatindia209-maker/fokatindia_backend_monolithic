package com.fokatindia.dto.vendor;

// ================================
// DTOs
// ================================


import lombok.Data;

@Data
public class ServiceRequest {

    private Long categoryId;

    private String name;

    private String description;

    private Double price;

    private Double discountedPrice;

    private Integer durationMinutes;

    private String imageUrl;

    private Boolean featured;

    private String serviceType;
}
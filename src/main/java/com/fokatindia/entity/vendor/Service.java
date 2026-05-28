package com.fokatindia.entity.vendor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("service_categories")
public class Service {
    @Id
    private Long id;

    // FK -> categories.id
    private Long categoryId;

    // Full Home Deep Cleaning
    private String name;

    // Service details
    private String description;

    // Base service price
    private Double price;

    // Discounted price
    private Double discountedPrice;

    // Service duration in minutes
    private Integer durationMinutes;

    // Service image
    private String imageUrl;

    // Service code
    // Example: HC001
    private String serviceCode;

    // Popular service
    private Boolean featured;

    // Available or not
    private Boolean active;

    // Average rating
    private Double rating;

    // Total bookings count
    private Integer totalBookings;

    // Total reviews count
    private Integer totalReviews;

    // Home / Salon / Cleaning etc
    private String serviceType;

    // SEO slug
    private String slug;

    // Audit fields
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

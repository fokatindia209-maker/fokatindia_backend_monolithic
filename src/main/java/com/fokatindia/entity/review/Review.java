package com.fokatindia.entity.review;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("reviews")
public class Review {

    @Id
    private Long id;

    private Long bookingId;

    private Long userId;

    private Long vendorId;

    private Long serviceId;

    private Integer rating; // 1 to 5

    private String comment;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
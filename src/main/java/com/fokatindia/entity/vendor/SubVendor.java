package com.fokatindia.entity.vendor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sub_vendors")
public class SubVendor {

    @Id
    private Long subVendorId;

    // Reference from users table
    private Long userId;

    // Parent vendor
    private Long vendorId;

    private String specialization;

    private Double latitude;
    private Double longitude;
    private Double serviceRadiusKm;

    private Integer experienceYears;

    // AVAILABLE, BUSY, INACTIVE
    private String availabilityStatus;

    private Double rating;

    private LocalDateTime createdAt;
}

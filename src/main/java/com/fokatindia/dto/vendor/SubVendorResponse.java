package com.fokatindia.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubVendorResponse {

    private Long userId;
    private Long subVendorId;
    private Long vendorId;

    private String specialization;
    private Integer experienceYears;
    private String availabilityStatus;
    private Double rating;
    private LocalDateTime createdAt;

    // USER DATA
    private String name;
    private String email;
    private String phone;
    private String status;
}
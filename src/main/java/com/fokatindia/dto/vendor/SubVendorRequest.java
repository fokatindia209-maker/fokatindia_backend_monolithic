package com.fokatindia.dto.vendor;

import lombok.Data;

@Data
public class SubVendorRequest {
    private Long userId;
    private Long vendorId;
    private String specialization;

    private Double latitude;
    private Double longitude;
    private Double serviceRadiusKm;


    private Integer experienceYears;
    private String availabilityStatus;
    private Double rating;
}
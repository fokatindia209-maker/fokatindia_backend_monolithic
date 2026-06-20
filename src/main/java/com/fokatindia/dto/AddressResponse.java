package com.fokatindia.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddressResponse {

    private Long addressId;

    private Long userId;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    private String country;

    private Double latitude;

    private Double longitude;

    private Boolean isDefault;

    private LocalDateTime createdAt;
}
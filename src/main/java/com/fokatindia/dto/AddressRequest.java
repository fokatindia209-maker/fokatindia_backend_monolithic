package com.fokatindia.dto;

import lombok.Data;

@Data
public class AddressRequest {

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
}
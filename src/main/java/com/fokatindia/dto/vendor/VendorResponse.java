package com.fokatindia.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorResponse {
    private Long vendorId;

    private Long userId;

    private String businessName;

    private String gstNumber;

    private String address;

    private String city;

    private String serviceArea;

    private String kycStatus;

    private Double rating;


    private String name;

    private String email;

    private String phone;

    private String status;

    public VendorResponse(Long vendorId, Long userId, String businessName, String gstNumber, String address, String city, String serviceArea, String kycStatus, Double rating) {
        this.vendorId = vendorId;
        this.userId = userId;
        this.businessName = businessName;
        this.gstNumber = gstNumber;
        this.address = address;
        this.city = city;
        this.serviceArea = serviceArea;
        this.kycStatus = kycStatus;
        this.rating = rating;
    }
}

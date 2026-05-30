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

}

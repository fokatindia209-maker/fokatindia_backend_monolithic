package com.fokatindia.dto.vendor;

import lombok.Data;

@Data
public class VendorRequest {


    private Long userId;

    private String businessName;

    private String gstNumber;

    private String address;

    private String city;

    private String serviceArea;
}

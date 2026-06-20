package com.fokatindia.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorResponse {
    private Long vendorId;

    private Long userId;

    private String businessName;

    private String gstNumber;



    private String serviceArea;

    private String kycStatus;

    private Double rating;

    private LocalDateTime createdAt;

    private String name;

    private String email;

    private String phone;

    private String status;

}

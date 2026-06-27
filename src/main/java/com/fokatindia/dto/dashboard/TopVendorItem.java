package com.fokatindia.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopVendorItem {
    private Long vendorId;
    private String businessName;
    private Double rating;
    private String kycStatus;
}

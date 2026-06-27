package com.fokatindia.dto.dashboard.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorTopSubVendorItem {
    private Long subVendorId;
    private String name;
    private String specialization;
    private String availabilityStatus;
    private Double rating;
    private Long totalJobs;
}

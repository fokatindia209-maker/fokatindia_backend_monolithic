package com.fokatindia.entity.vendor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("vendors")
public class Vendor {

    @Id
    private Long vendorId;

    private Long userId;

    private String businessName;

    private String gstNumber;

    private String serviceArea;

    // PENDING, VERIFIED, REJECTED
    private String kycStatus;

    private Double rating;

    private LocalDateTime createdAt;
}

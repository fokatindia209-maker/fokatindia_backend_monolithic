package com.fokatindia.dto.dashboard.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorRecentBookingItem {
    private Long id;
    private String bookingCode;
    private String userName;
    private String bookingStatus;
    private String paymentStatus;
    private Double finalAmount;
    private LocalDate bookingDate;
}

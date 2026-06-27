package com.fokatindia.dto.dashboard.subvendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubVendorRecentBookingItem {
    private Long id;
    private String bookingCode;
    private String userName;
    private String bookingStatus;
    private String paymentStatus;
    private Double finalAmount;
    private Double subVendorAmount;
    private LocalDate bookingDate;
}

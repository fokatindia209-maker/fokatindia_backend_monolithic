package com.fokatindia.dto.booking;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {

    private Long userId;
    private Long vendorId;
    private Long subVendorId;
    private Long categoryId;
    private Long serviceId;
    private Long addressId;
    private LocalDate bookingDate;
    private String bookingTime;
    private Double amount;
    private Double discountAmount;
    private Double finalAmount;
    private String notes;
    private String otp;
    private Boolean active;
}
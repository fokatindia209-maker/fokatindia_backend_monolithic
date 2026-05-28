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

    private LocalDate bookingDate;

    private String bookingTime;

    private String address;

    private String city;

    private String pincode;

    private Double latitude;

    private Double longitude;

    private Double amount;

    private Double discountAmount;

    private Double finalAmount;

    private String notes;
}
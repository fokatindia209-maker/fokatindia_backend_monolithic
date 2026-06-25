package com.fokatindia.entity.booking;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table("bookings")
public class Booking {

    @Id
    private Long id;

    private String bookingCode;

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


    private Double companyAmount;

    private Double vendorAmount;

    private Double subVendorAmount;

    private String paymentStatus;

    private String bookingStatus;

    private String notes;

    private String otp;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean earningsGenerated;
}
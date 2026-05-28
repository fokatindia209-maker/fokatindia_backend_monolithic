package com.fokatindia.dto.booking;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponse {

    private Long id;

    private String bookingCode;

    private Long userId;

    private Long vendorId;

    private Long serviceId;

    private LocalDate bookingDate;

    private String bookingTime;

    private Double finalAmount;

    private String bookingStatus;

    private String paymentStatus;

    private LocalDateTime createdAt;
}
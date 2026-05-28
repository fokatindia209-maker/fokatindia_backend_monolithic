package com.fokatindia.dto.payment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long id;

    // =========================================
    // BOOKING RELATION
    // =========================================


    private Long bookingId;
    private Long userId;

    private Double amount;
    private String currency;

    private String paymentMethod;
    private String paymentStatus;

    private String gateway;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private Boolean refunded;
    private Double refundAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
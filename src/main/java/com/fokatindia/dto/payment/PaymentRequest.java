package com.fokatindia.dto.payment;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long bookingId;
    private Long userId;

    private Double amount;
    private String currency;

    private String paymentMethod; // CARD, UPI, WALLET

    private String gateway; // RAZORPAY

    // Razorpay fields (optional in request)
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private Boolean refunded;
    private Double refundAmount;
}
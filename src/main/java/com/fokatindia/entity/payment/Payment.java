package com.fokatindia.entity.payment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("payments")
public class Payment {

    @Id
    private Long id;

    private Long bookingId;

    private Long userId;

    // Gateway order id (Razorpay/Stripe)
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;





    private Double amount;
    private String currency;

    private String paymentMethod; // CARD, UPI, WALLET
    private String paymentStatus; // PENDING, SUCCESS, FAILED, REFUNDED

    private String gateway; // RAZORPAY / STRIPE


    private Boolean refunded;

    private Double refundAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
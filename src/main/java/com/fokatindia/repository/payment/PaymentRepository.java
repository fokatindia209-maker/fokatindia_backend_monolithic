package com.fokatindia.repository.payment;

import com.fokatindia.entity.payment.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {

    Flux<Payment> findByUserId(Long userId);

    Flux<Payment> findByBookingId(Long bookingId);
    Mono<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Mono<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
}
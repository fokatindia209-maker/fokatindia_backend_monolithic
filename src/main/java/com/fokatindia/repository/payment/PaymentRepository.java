package com.fokatindia.repository.payment;

import com.fokatindia.entity.payment.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {

    Flux<Payment> findByUserId(Long userId);

    Flux<Payment> findByBookingId(Long bookingId);
}
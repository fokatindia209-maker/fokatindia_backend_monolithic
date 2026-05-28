package com.fokatindia.service.payment;


import com.fokatindia.dto.payment.PaymentRequest;
import com.fokatindia.dto.payment.PaymentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<PaymentResponse> create(PaymentRequest request);

    Mono<PaymentResponse> getById(Long id);

    Flux<PaymentResponse> getAll();

    Flux<PaymentResponse> getByUser(Long userId);

    Flux<PaymentResponse> getByBooking(Long bookingId);

    Mono<PaymentResponse> updateStatus(Long id, String status);

    Mono<PaymentResponse> refund(Long id, Double amount);

    Mono<Void> delete(Long id);
}
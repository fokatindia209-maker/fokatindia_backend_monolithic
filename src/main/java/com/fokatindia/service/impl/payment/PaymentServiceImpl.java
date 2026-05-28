package com.fokatindia.service.impl.payment;


import com.fokatindia.dto.payment.PaymentRequest;
import com.fokatindia.dto.payment.PaymentResponse;
import com.fokatindia.entity.payment.Payment;
import com.fokatindia.repository.payment.PaymentRepository;
import com.fokatindia.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    @Override
    public Mono<PaymentResponse> create(PaymentRequest request) {

        Payment payment = new Payment();

        BeanUtils.copyProperties(request, payment);

        payment.setPaymentStatus("PENDING");
        payment.setRefunded(false);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return repository.save(payment)
                .map(this::mapToResponse);
    }

    @Override
    public Mono<PaymentResponse> getById(Long id) {

        return repository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public Flux<PaymentResponse> getAll() {

        return repository.findAll()
                .map(this::mapToResponse);
    }

    @Override
    public Flux<PaymentResponse> getByUser(Long userId) {

        return repository.findByUserId(userId)
                .map(this::mapToResponse);
    }

    @Override
    public Flux<PaymentResponse> getByBooking(Long bookingId) {

        return repository.findByBookingId(bookingId)
                .map(this::mapToResponse);
    }

    @Override
    public Mono<PaymentResponse> updateStatus(Long id, String status) {

        return repository.findById(id)
                .flatMap(payment -> {

                    payment.setPaymentStatus(status);
                    payment.setUpdatedAt(LocalDateTime.now());

                    return repository.save(payment);
                })
                .map(this::mapToResponse);
    }

    @Override
    public Mono<PaymentResponse> refund(Long id, Double amount) {

        return repository.findById(id)
                .flatMap(payment -> {

                    payment.setRefunded(true);
                    payment.setRefundAmount(amount);
                    payment.setPaymentStatus("REFUNDED");
                    payment.setUpdatedAt(LocalDateTime.now());

                    return repository.save(payment);
                })
                .map(this::mapToResponse);
    }

    @Override
    public Mono<Void> delete(Long id) {

        return repository.deleteById(id);
    }

    // =====================================================
    // MAPPER
    // =====================================================

    private PaymentResponse mapToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        BeanUtils.copyProperties(payment, response);

        return response;
    }
}
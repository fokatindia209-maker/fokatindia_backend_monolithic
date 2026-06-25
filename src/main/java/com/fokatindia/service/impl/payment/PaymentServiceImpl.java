package com.fokatindia.service.impl.payment;


import com.fokatindia.dto.payment.PaymentRequest;
import com.fokatindia.dto.payment.PaymentResponse;
import com.fokatindia.entity.payment.Payment;
import com.fokatindia.repository.booking.BookingRepository;
import com.fokatindia.repository.payment.PaymentRepository;
import com.fokatindia.service.payment.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    private final BookingRepository bookingRepository;


    @Value("${razorpay.key-id}")
    private String razorpayKeyId;


    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    @Override
    public Mono<PaymentResponse> create(PaymentRequest request) {

try{
        RazorpayClient razorpay =
                new RazorpayClient(
                        razorpayKeyId,
                        razorpaySecret
                );


        JSONObject options = new JSONObject();

        options.put(
                "amount",
                (int) (request.getAmount() * 100)
        );

        options.put(
                "currency",
                request.getCurrency()
        );

        Order order =
                razorpay.orders.create(options);

        Payment payment = new Payment();

        BeanUtils.copyProperties(request, payment);
    payment.setRazorpayOrderId(
            order.get("id")
    );

        payment.setPaymentStatus("PENDING");
        payment.setRefunded(false);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return repository.save(payment)
                .map(this::mapToResponse);

    } catch (Exception e) {

        return Mono.error(
                new RuntimeException(
                        "Unable to create Razorpay order",
                        e
                )
        );
    }
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


    @Override
    public Mono<PaymentResponse> verifyPayment(PaymentRequest request) {

        return repository.findByRazorpayOrderId(
                        request.getRazorpayOrderId()
                )
                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException("Payment not found")
                        )
                )
                .flatMap(payment -> {

                    try {

                        String payload =
                                request.getRazorpayOrderId()
                                        + "|"
                                        + request.getRazorpayPaymentId();

                        boolean verified = Utils.verifySignature(
                                payload,
                                request.getRazorpaySignature(),
                                razorpaySecret
                        );

                        if (!verified) {
                            return Mono.error(
                                    new RuntimeException(
                                            "Invalid payment signature"
                                    )
                            );
                        }

                        payment.setRazorpayPaymentId(
                                request.getRazorpayPaymentId()
                        );

                        payment.setRazorpaySignature(
                                request.getRazorpaySignature()
                        );

                        payment.setPaymentStatus("SUCCESS");
                        payment.setUpdatedAt(LocalDateTime.now());

//                        return repository.save(payment);

                        return repository.save(payment)

                                // =========================
                                // UPDATE BOOKING ALSO
                                // =========================
                                .flatMap(savedPayment -> {

                                    return bookingRepository.findById(savedPayment.getBookingId())
                                            .flatMap(booking -> {

                                                booking.setPaymentStatus("SUCCESS");

                                                return bookingRepository.save(booking);
                                            })
                                            .thenReturn(savedPayment);
                                });

                    } catch (Exception e) {

                        return Mono.error(
                                new RuntimeException(
                                        "Payment verification failed"
                                )
                        );
                    }
                })
                .map(this::mapToResponse);
    }
}
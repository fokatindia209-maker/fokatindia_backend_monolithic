package com.fokatindia.controller.payment;


import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.payment.PaymentRequest;
import com.fokatindia.dto.payment.PaymentResponse;
import com.fokatindia.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    // =====================================================
    // CREATE PAYMENT
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_CREATE')")
    @PostMapping
    public Mono<ApiResponse<PaymentResponse>> create(
            @RequestBody PaymentRequest request
    ) {

        return service.create(request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Payment created successfully",
                        res
                ));
    }

    // =====================================================
    // GET PAYMENT BY ID
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @GetMapping("/{id}")
    public Mono<ApiResponse<PaymentResponse>> getById(
            @PathVariable Long id
    ) {

        return service.getById(id)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Payment fetched successfully",
                        res
                ))
                .defaultIfEmpty(new ApiResponse<>(
                        "error",
                        404,
                        "Payment not found",
                        null
                ));
    }

    // =====================================================
    // GET ALL PAYMENTS
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_VIEW_ALL')")
    @GetMapping
    public Mono<ApiResponse<List<PaymentResponse>>> getAll() {

        return service.getAll()
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Payment list fetched successfully",
                        list
                ));
    }

    // =====================================================
    // GET BY USER
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @GetMapping("/user/{userId}")
    public Mono<ApiResponse<List<PaymentResponse>>> getByUser(
            @PathVariable Long userId
    ) {

        return service.getByUser(userId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "User payments fetched successfully",
                        list
                ));
    }

    // =====================================================
    // GET BY BOOKING
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @GetMapping("/booking/{bookingId}")
    public Mono<ApiResponse<List<PaymentResponse>>> getByBooking(
            @PathVariable Long bookingId
    ) {

        return service.getByBooking(bookingId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Booking payments fetched successfully",
                        list
                ));
    }

    // =====================================================
    // UPDATE PAYMENT STATUS
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_UPDATE')")
    @PutMapping("/{id}/status")
    public Mono<ApiResponse<PaymentResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        return service.updateStatus(id, status)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Payment status updated successfully",
                        res
                ));
    }

    // =====================================================
    // REFUND PAYMENT
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_REFUND')")
    @PutMapping("/{id}/refund")
    public Mono<ApiResponse<PaymentResponse>> refund(
            @PathVariable Long id,
            @RequestParam Double amount
    ) {

        return service.refund(id, amount)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Payment refunded successfully",
                        res
                ));
    }

    // =====================================================
    // DELETE PAYMENT
    // =====================================================

    @PreAuthorize("hasAuthority('PAYMENT_DELETE')")
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> delete(
            @PathVariable Long id
    ) {

        return service.delete(id)
                .thenReturn(new ApiResponse<>(
                        "success",
                        200,
                        "Payment deleted successfully",
                        "Deleted"
                ));
    }
}
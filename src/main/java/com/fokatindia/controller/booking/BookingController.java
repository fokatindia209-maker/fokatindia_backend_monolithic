package com.fokatindia.controller.booking;



import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.booking.BookingRequest;
import com.fokatindia.dto.booking.BookingResponse;
import com.fokatindia.service.booking.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    // =====================================================
    // CREATE BOOKING
    // =====================================================

    @PreAuthorize("hasAuthority('BOOKING_CREATE')")
    @PostMapping
    public Mono<ApiResponse<BookingResponse>> create(
            @RequestBody BookingRequest request
    ) {

        return service.create(request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Booking created successfully",
                        res
                ));
    }


    // =====================================================
    // GET BOOKING BY ID
    // =====================================================

    @PreAuthorize("hasAuthority('BOOKING_VIEW_OWN')")
    @GetMapping("/{id}")
    public Mono<ApiResponse<BookingResponse>> getById(
            @PathVariable Long id
    ) {

        return service.getById(id)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Booking fetched successfully",
                        res
                ))
                .defaultIfEmpty(new ApiResponse<>(
                        "error",
                        404,
                        "Booking not found",
                        null
                ));
    }


    // =====================================================
    // GET ALL BOOKINGS
    // =====================================================

    @PreAuthorize("hasAuthority('BOOKING_VIEW_ALL')")
    @GetMapping
    public Mono<ApiResponse<List<BookingResponse>>> getAll() {

        return service.getAll()
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "Booking list fetched successfully",
                        list
                ));
    }

    // =====================================================
    // GET BY USER
    // =====================================================

    @PreAuthorize("hasAuthority('BOOKING_VIEW')")
    @GetMapping("/user/{userId}")
    public Mono<ApiResponse<List<BookingResponse>>> getByUser(
            @PathVariable Long userId
    ) {

        return service.getByUser(userId)
                .collectList()
                .map(list -> new ApiResponse<>(
                        "success",
                        200,
                        "User bookings fetched successfully",
                        list
                ));
    }

    // =====================================================
    // UPDATE STATUS (BOOKING / PAYMENT)
    // =====================================================

    @PreAuthorize("hasAuthority('BOOKING_UPDATE')")
    @PutMapping("/{id}/status")
    public Mono<ApiResponse<BookingResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String bookingStatus,
            @RequestParam(required = false) String paymentStatus
    ) {

        return service.updateStatus(id, bookingStatus, paymentStatus)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Booking status updated successfully",
                        res
                ));
    }

    // =====================================================
    // CANCEL BOOKING
    // =====================================================

    @PreAuthorize("hasAuthority('BOOKING_CANCEL')")
    @PutMapping("/{id}/cancel")
    public Mono<ApiResponse<BookingResponse>> cancel(
            @PathVariable Long id
    ) {

        return service.cancel(id)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Booking cancelled successfully",
                        res
                ));
    }

    // =====================================================
    // DELETE BOOKING
    // =====================================================

    @PreAuthorize("hasAuthority('BOOKING_DELETE')")
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> delete(
            @PathVariable Long id
    ) {

        return service.delete(id)
                .thenReturn(new ApiResponse<>(
                        "success",
                        200,
                        "Booking deleted successfully",
                        "Deleted"
                ));
    }

}

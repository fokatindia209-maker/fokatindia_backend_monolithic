package com.fokatindia.service.booking;


import com.fokatindia.dto.booking.BookingRequest;
import com.fokatindia.dto.booking.BookingResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingService {
    Mono<BookingResponse> create(BookingRequest request);

    Mono<BookingResponse> getById(Long id);

    Flux<BookingResponse> getAll();

    Flux<BookingResponse> getByUser(Long userId);

    Flux<BookingResponse> getByVendor(Long vendorId);

    Mono<BookingResponse> updateStatus(Long id, String bookingStatus, String paymentStatus);

    Mono<BookingResponse> cancel(Long id);

    Mono<Void> delete(Long id);

    Flux<BookingResponse> getByVendorId(Long vendorId);


    Flux<BookingResponse> getBySubVendorId(Long subVendorId);

}

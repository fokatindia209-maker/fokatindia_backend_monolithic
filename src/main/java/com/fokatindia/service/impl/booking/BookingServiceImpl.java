package com.fokatindia.service.impl.booking;



import com.fokatindia.dto.booking.BookingRequest;
import com.fokatindia.dto.booking.BookingResponse;
import com.fokatindia.entity.booking.Booking;
import com.fokatindia.exception.ResourceNotFoundException;
import com.fokatindia.repository.booking.BookingRepository;
import com.fokatindia.service.booking.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    @Override
    public Mono<BookingResponse> create(BookingRequest request) {
        return Mono.just(request)
                .flatMap(req -> {

                    Booking booking = new Booking();

                    booking.setBookingCode("BK-" + UUID.randomUUID().toString().substring(0, 8));

                    booking.setUserId(req.getUserId());
                    booking.setVendorId(req.getVendorId());
                    booking.setSubVendorId(req.getSubVendorId());
                    booking.setCategoryId(req.getCategoryId());
                    booking.setServiceId(req.getServiceId());
                    booking.setAddressId(req.getAddressId());
                    booking.setBookingDate(req.getBookingDate());
                    booking.setBookingTime(req.getBookingTime());
                    booking.setAmount(req.getAmount());
                    booking.setDiscountAmount(req.getDiscountAmount());
                    booking.setFinalAmount(req.getFinalAmount());
                    booking.setNotes(req.getNotes());

                    booking.setBookingStatus("PENDING");
                    booking.setPaymentStatus("PENDING");

                    booking.setActive(true);
                    String otp = String.valueOf((int)(Math.random() * 9000) + 1000);
                    booking.setOtp(otp);
                    booking.setCreatedAt(LocalDateTime.now());
                    booking.setUpdatedAt(LocalDateTime.now());

                    // 🔥 PROPER LOGGING INSIDE FLOW
                    return repository.save(booking);
                })
                .map(this::mapToResponse);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @Override
    public Mono<BookingResponse> getById(Long id) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException("Booking not found")
                        )
                )

                .map(this::mapToResponse);
    }

    // =====================================================
    // GET ALL BOOKINGS
    // =====================================================
    @Override
    public Flux<BookingResponse> getAll() {

        return repository.findAll()
                .map(this::mapToResponse);
    }

    // =====================================================
    // GET BY USER
    // =====================================================
    @Override
    public Flux<BookingResponse> getByUser(Long userId) {

        return repository.findByUserId(userId)
                .map(this::mapToResponse);
    }

    @Override
    public Flux<BookingResponse> getByVendorId(Long vendorId) {

        return repository.findByVendorId(vendorId)
                .map(this::mapToResponse);
    }

    @Override
    public Flux<BookingResponse> getBySubVendorId(Long subVendorId) {

        return repository.findBySubVendorId(subVendorId)
                .map(this::mapToResponse);
    }


    // =====================================================
    // GET BY VENDOR
    // =====================================================
    @Override
    public Flux<BookingResponse> getByVendor(Long vendorId) {

        return repository.findByVendorId(vendorId)
                .map(this::mapToResponse);
    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================
    @Override
    public Mono<BookingResponse> updateStatus(Long id, String bookingStatus, String paymentStatus) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException("Booking not found")
                        )
                )

                .flatMap(b -> {
                    log.info("Booking1 Status: {}", b.getBookingStatus());
                    log.info("Booking1 Status: {}", b.getPaymentStatus());
                    log.info("Booking1 Generated: {}", b.getEarningsGenerated());

                    if (bookingStatus != null) {
                        b.setBookingStatus(bookingStatus);
                    }

                    if (paymentStatus != null) {
                        b.setPaymentStatus(paymentStatus);
                    }

                    // Generate earnings once

                    // =========================
                    // EARNINGS GENERATION
                    // =========================
                    if (
                            "SUCCESS".equalsIgnoreCase(b.getBookingStatus()) &&
                                    "SUCCESS".equalsIgnoreCase(b.getPaymentStatus()) &&
                                    !Boolean.TRUE.equals(b.getEarningsGenerated())
                    ) {
                        calculateEarnings(b);
                        b.setEarningsGenerated(true);
                    }



                    b.setUpdatedAt(LocalDateTime.now());

                    return repository.save(b);
                })

                .map(this::mapToResponse);
    }


    // =====================================================
    // CANCEL BOOKING
    // =====================================================
    @Override
    public Mono<BookingResponse> cancel(Long id) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException("Booking not found")
                        )
                )

                .flatMap(b -> {

                    b.setBookingStatus("CANCELLED");
                    b.setUpdatedAt(LocalDateTime.now());

                    return repository.save(b);
                })

                .map(this::mapToResponse);
    }


    // =====================================================
    // DELETE BOOKING
    // =====================================================
    @Override
    public Mono<Void> delete(Long id) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException("Booking not found")
                        )
                )

                .flatMap(repository::delete);
    }




    // =====================================================
    // MAPPER
    // =====================================================
    private BookingResponse mapToResponse(Booking b) {

        BookingResponse r = new BookingResponse();

        r.setId(b.getId());
        r.setBookingCode(b.getBookingCode());
        r.setUserId(b.getUserId());
        r.setVendorId(b.getVendorId());
        r.setServiceId(b.getServiceId());
        r.setSubVendorId(b.getSubVendorId());
        r.setAddressId(b.getAddressId());
        r.setCategoryId(b.getCategoryId());
        r.setBookingDate(b.getBookingDate());
        r.setBookingTime(b.getBookingTime());
        r.setFinalAmount(b.getFinalAmount());
        r.setCompanyAmount(b.getCompanyAmount());
        r.setVendorAmount(b.getVendorAmount());
        r.setSubVendorAmount(b.getSubVendorAmount());
        r.setEarningsGenerated(b.getEarningsGenerated());
        r.setBookingStatus(b.getBookingStatus());
        r.setPaymentStatus(b.getPaymentStatus());
        r.setOtp(b.getOtp());
        r.setNotes(b.getNotes());
        r.setActive(b.getActive());
        r.setCreatedAt(b.getCreatedAt());
        return r;
    }


    private void calculateEarnings(Booking booking) {

        double amount = booking.getFinalAmount();
        double company = amount * 0.30;   // 20%
        double subVendor = amount * 0.60;    // 70%
        double vendor = amount * 0.10; // 10%

        booking.setCompanyAmount(company);
        booking.setVendorAmount(vendor);
        booking.setSubVendorAmount(subVendor);


        log.info("💰 Earnings Calculated -> company={}, vendor={}, subVendor={}",
                company, vendor, subVendor);
    }
}

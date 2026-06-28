package com.fokatindia.service.impl.vendor;


import com.fokatindia.dto.vendor.SubVendorResponse;
import com.fokatindia.dto.vendor.VendorRequest;
import com.fokatindia.dto.vendor.VendorResponse;
import com.fokatindia.entity.vendor.SubVendor;
import com.fokatindia.entity.vendor.Vendor;
import com.fokatindia.exception.ResourceNotFoundException;
import com.fokatindia.repository.UserRepository;
import com.fokatindia.repository.vendor.VendorRepository;
import com.fokatindia.service.vendor.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepo;
    private final UserRepository userRepository;


    // =====================================================
    // VENDOR
    // =====================================================


    @Override
    public Mono<VendorResponse> createVendor(
            VendorRequest request
    ) {

        Vendor vendor = new Vendor();

        vendor.setUserId(request.getUserId());

        vendor.setBusinessName(
                request.getBusinessName()
        );

        vendor.setGstNumber(
                request.getGstNumber()
        );

//        vendor.setAddress(
//                request.getAddress()
//        );
//
//        vendor.setCity(
//                request.getCity()
//        );

        vendor.setServiceArea(
                request.getServiceArea()
        );

        vendor.setKycStatus("PENDING");

        vendor.setRating(0.0);

        vendor.setCreatedAt(LocalDateTime.now());

        return vendorRepo.save(vendor)

                .flatMap(this::mapToResponse);
    }
    @Override
    public Mono<VendorResponse> getVendor(Long id) {

        return vendorRepo.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        )
                )

                .flatMap(this::mapToResponse);
    }




    @Override
    public Mono<VendorResponse> getVendorByUserId(Long userId) {

        return vendorRepo.findByUserId(userId)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        )
                )

                .flatMap(this::mapToResponse);
    }

    @Override
    public Flux<VendorResponse> getAllVendors() {
        return vendorRepo.findAll()
                .flatMap(this::mapToResponse);
    }


    @Override
    public Flux<VendorResponse> getAllVendorsWithUserId() {
        return vendorRepo.findAllVendorDetailWithUserId();
    }


    @Override
    public Mono<VendorResponse> updateVendor(
            Long id,
            VendorRequest request
    ) {

        return vendorRepo.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        )
                )

                .flatMap(vendor -> {

                    if (request.getBusinessName() != null) {
                        vendor.setBusinessName(
                                request.getBusinessName()
                        );
                    }

//                    if (request.getAddress() != null) {
//                        vendor.setAddress(
//                                request.getAddress()
//                        );
//                    }
//
//                    if (request.getCity() != null) {
//                        vendor.setCity(
//                                request.getCity()
//                        );
//                    }

                    if (request.getServiceArea() != null) {
                        vendor.setServiceArea(
                                request.getServiceArea()
                        );
                    }

                    return vendorRepo.save(vendor);
                })

                .flatMap(this::mapToResponse);
    }

    @Override
    public Mono<Void> deleteVendor(Long id) {
        return vendorRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vendor not found")))
                .flatMap(vendorRepo::delete);
    }

    @Override
    public Mono<VendorResponse> deactivateVendor(Long id) {
        return vendorRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vendor not found")))
                .flatMap(vendor -> userRepository.findById(vendor.getUserId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                        .flatMap(user -> {
                            user.setStatus("DEACTIVE");
                            return userRepository.save(user);
                        })
                        .thenReturn(vendor)
                )
                .flatMap(this::mapToResponse);
    }

    // =====================================================
    // MAPPERS
    // =====================================================


    // =====================================================
    // MAPPER (SubVendor + User)
    // =====================================================
    private Mono<VendorResponse> mapToResponse(Vendor vendor) {

        return userRepository.findById(vendor.getUserId())
                .map(user -> new VendorResponse(
                        vendor.getVendorId(),
                        vendor.getUserId(),
                        vendor.getBusinessName(),
                        vendor.getGstNumber(),
//                        vendor.getAddress(),
//                        vendor.getCity(),
                        vendor.getServiceArea(),
                        vendor.getKycStatus(),
                        vendor.getRating(),
                        vendor.getCreatedAt(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getStatus()
                ))
                .switchIfEmpty(
                        Mono.just(new VendorResponse(
                                vendor.getVendorId(),
                                vendor.getUserId(),
                                vendor.getBusinessName(),
                                vendor.getGstNumber(),

                                vendor.getServiceArea(),
                                vendor.getKycStatus(),
                                vendor.getRating(),
                                vendor.getCreatedAt(),
                                null,
                                null,
                                null,
                                null
                        ))
                );
    }
}

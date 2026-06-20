package com.fokatindia.service.impl.vendor;


import com.fokatindia.dto.vendor.CategoryResponse;
import com.fokatindia.dto.vendor.SubVendorRequest;
import com.fokatindia.dto.vendor.SubVendorResponse;
import com.fokatindia.entity.vendor.SubVendor;
import com.fokatindia.exception.ResourceNotFoundException;
import com.fokatindia.repository.UserRepository;
import com.fokatindia.repository.vendor.SubVendorRepository;
import com.fokatindia.repository.vendor.VendorRepository;
import com.fokatindia.service.vendor.SubVendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Slf4j
public class SubVendorServiceImpl implements SubVendorService {
    private final SubVendorRepository subVendorRepository;
    private final VendorRepository vendorRepository;
    private  final UserRepository userRepository;
    @Override
    public Mono<SubVendorResponse> addSubVendor(
            SubVendorRequest request
    ) {

        return vendorRepository.findById(request.getVendorId())
                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException("Vendor not found")
                        )
                )
                .flatMap(vendor -> {

                    SubVendor subVendor = new SubVendor();

                    subVendor.setUserId(request.getUserId());
                    subVendor.setVendorId(request.getVendorId());
                    subVendor.setSpecialization(
                            request.getSpecialization()
                    );
                    subVendor.setExperienceYears(
                            request.getExperienceYears()
                    );
                    subVendor.setAvailabilityStatus(
                            request.getAvailabilityStatus()
                    );
                    subVendor.setRating(
                            request.getRating()
                    );
                    subVendor.setCreatedAt(
                            LocalDateTime.now()
                    );

                    return subVendorRepository.save(subVendor)
                            .flatMap(this::mapToResponse);
                });
    }

    @Override
    public Flux<SubVendorResponse> getSubVendors(
            Long vendorId
    ) {

        return subVendorRepository.findByVendorId(
                        vendorId
                )

                .flatMap(this::mapToResponse);
    }

    @Override
    public Flux<SubVendorResponse> getSubVendorsWithUser(
            Long vendorId
    ) {

        return subVendorRepository.findSubVendorsWithUser(vendorId).flatMap(this::mapToResponse);
    }


    @Override
    public Flux<SubVendorResponse> getSubVendorByServiceId(
            Long serviceId,
            Double lat,
            Double lng
    ) {

        return subVendorRepository.findByServiceId(serviceId)
                .filter(subVendor -> {

                    if (subVendor.getLatitude() == null ||
                            subVendor.getLongitude() == null) {
                        return false;
                    }

                    double distance = distanceKm(
                            lat,
                            lng,
                            subVendor.getLatitude(),
                            subVendor.getLongitude()
                    );

                    double radius = subVendor.getServiceRadiusKm() == null
                            ? 10
                            : subVendor.getServiceRadiusKm();

                    return distance <= radius;
                })
                .flatMap(this::mapToResponse);
    }

    @Override
    public Mono<SubVendorResponse> getSubVendorBySubVendorId(Long subVendorId) {
        return subVendorRepository.findById(subVendorId)
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "SubVendor not found"
                                )
                        )
                )
                .flatMap(this::mapToResponse);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public Mono<SubVendorResponse> updateSubVendor(
            Long id,
            SubVendorRequest request
    ) {

        return subVendorRepository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "SubVendor not found"
                                )
                        )
                )

                .flatMap(subVendor -> {

                    if (request.getSpecialization() != null) {

                        subVendor.setSpecialization(
                                request.getSpecialization()
                        );
                    }

                    if (request.getExperienceYears() != null) {

                        subVendor.setExperienceYears(
                                request.getExperienceYears()
                        );
                    }

                    if (request.getAvailabilityStatus() != null) {

                        subVendor.setAvailabilityStatus(
                                request.getAvailabilityStatus()
                        );
                    }

                    if (request.getRating() != null) {

                        subVendor.setRating(
                                request.getRating()
                        );
                    }

                    return subVendorRepository.save(
                                    subVendor
                            )

                            .flatMap(this::mapToResponse);
                });
    }

    @Override
    public Flux<SubVendorResponse> getAllSubVendors() {
        return subVendorRepository.findAll()
                .flatMap(this::mapToResponse);
    }


    // =====================================================
    // MAPPER (SubVendor + User)
    // =====================================================
    private Mono<SubVendorResponse> mapToResponse(SubVendor subVendor) {

        return userRepository.findById(subVendor.getUserId())
                .map(user -> new SubVendorResponse(
                        subVendor.getUserId(),
                        subVendor.getSubVendorId(),
                        subVendor.getVendorId(),
                        subVendor.getSpecialization(),
                        subVendor.getExperienceYears(),
                        subVendor.getAvailabilityStatus(),
                        subVendor.getRating(),
                        subVendor.getCreatedAt(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getStatus()
                ))
                .switchIfEmpty(
                        Mono.just(new SubVendorResponse(
                                subVendor.getUserId(),
                                subVendor.getSubVendorId(),
                                subVendor.getVendorId(),
                                subVendor.getSpecialization(),
                                subVendor.getExperienceYears(),
                                subVendor.getAvailabilityStatus(),
                                subVendor.getRating(),
                                subVendor.getCreatedAt(),
                                null,
                                null,
                                null,
                                null
                        ))
                );
    }


    private double distanceKm(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

}

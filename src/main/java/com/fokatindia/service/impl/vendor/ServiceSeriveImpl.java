package com.fokatindia.service.impl.vendor;


import com.fokatindia.dto.vendor.CategoryResponse;
import com.fokatindia.dto.vendor.ServiceRequest;
import com.fokatindia.dto.vendor.ServiceResponse;
import com.fokatindia.entity.vendor.Category;
import com.fokatindia.entity.vendor.Service;
import com.fokatindia.exception.ResourceNotFoundException;
import com.fokatindia.repository.vendor.CategoryRepository;
import com.fokatindia.repository.vendor.ServiceRepository;
import com.fokatindia.service.CloudinaryService;
import com.fokatindia.service.vendor.ServiceService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceSeriveImpl implements ServiceService {

    private final ServiceRepository repository;
    private final CloudinaryService cloudinaryService;

    private final CategoryRepository categoryRepository;
    @Override
    public Mono<ServiceResponse> create(ServiceRequest request) {

        return categoryRepository.findById(request.getCategoryId())

                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Category not found")
                ))

                .flatMap(category ->

                        cloudinaryService.upload(request.getFile())

                                .flatMap(imageUrl -> {

                                    Service service = new Service();

                                    service.setCategoryId(request.getCategoryId());
                                    service.setName(request.getName());
                                    service.setDescription(request.getDescription());

                                    service.setPrice(request.getPrice());
                                    service.setDiscountedPrice(request.getDiscountedPrice());

                                    service.setTaxPercentage(
                                            request.getTaxPercentage() != null
                                                    ? request.getTaxPercentage()
                                                    : 18.0
                                    );
                                    service.setDurationMinutes(request.getDurationMinutes());

                                    service.setImageUrl(imageUrl); // ✅ FROM CLOUDINARY

                                    service.setFeatured(request.getFeatured());
                                    service.setServiceType(request.getServiceType());

                                    service.setServiceCode(generateCode());
                                    service.setActive(true);

                                    service.setRating(0.0);
                                    service.setTotalBookings(0);
                                    service.setTotalReviews(0);

                                    service.setCreatedAt(LocalDateTime.now());
                                    service.setUpdatedAt(LocalDateTime.now());

                                    return repository.save(service)
                                            .map(saved -> mapToResponse(saved, category));
                                })
                );
    }
//    @Override
//    public Mono<ServiceResponse> create(ServiceRequest request) {
//        return categoryRepository.findById(request.getCategoryId())
//                .switchIfEmpty(Mono.error(
//                        new ResourceNotFoundException(
//                                "Category not found"
//                        )
//                ))
//                .flatMap(category ->
//                        cloudinaryService.upload(request.getFile())
//                                .flatMap(imageUrl -> {
//                                    Service service = new Service();
//
//                                    service.setCategoryId(
//                                            request.getCategoryId()
//                                    );
//
//                                    service.setName(
//                                            request.getName()
//                                    );
//
//                                    service.setDescription(
//                                            request.getDescription()
//                                    );
//
//                                    service.setPrice(
//                                            request.getPrice()
//                                    );
//
//                                    service.setDiscountedPrice(
//                                            request.getDiscountedPrice()
//                                    );
//
//                                    service.setDurationMinutes(
//                                            request.getDurationMinutes()
//                                    );
//
//                                    service.setImageUrl(
//                                            imageUrl
//                                    );
//
//                                    service.setFeatured(
//                                            request.getFeatured()
//                                    );
//
//                                    service.setServiceType(
//                                            request.getServiceType()
//                                    );
//
//                                    service.setServiceCode(
//                                            generateCode()
//                                    );
//
//                                    service.setActive(true);
//
//                                    service.setRating(0.0);
//
//                                    service.setTotalBookings(0);
//
//                                    service.setTotalReviews(0);
//
//                                    service.setCreatedAt(
//                                            LocalDateTime.now()
//                                    );
//
//                                    service.setUpdatedAt(
//                                            LocalDateTime.now()
//                                    );
//                                    return repository.save(service)
//                                            .map(saved ->
//                                                    mapToResponse(
//                                                            saved,
//                                                            category
//                                                    )
//                                            );
//                                });
//    }



    // =====================================================
    // GET SERVICE BY ID
    // =====================================================

    @Override
    public Mono<ServiceResponse> getById(
            Long id
    ) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        )
                )

                .flatMap(service ->

                        categoryRepository.findById(
                                        service.getCategoryId()
                                )

                                .map(category ->
                                        mapToResponse(
                                                service,
                                                category
                                        )
                                )
                );
    }

    // =====================================================
    // GET ALL SERVICES
    // =====================================================

    @Override
    public Flux<ServiceResponse> getAll() {

        return repository.findAll()

                .flatMap(service ->

                        categoryRepository.findById(
                                        service.getCategoryId()
                                )

                                .map(category ->
                                        mapToResponse(
                                                service,
                                                category
                                        )
                                )
                );
    }

    // =====================================================
    // GET SERVICES BY CATEGORY
    // =====================================================

    @Override
    public Flux<ServiceResponse> getByCategory(
            Long categoryId
    ) {

        return repository.findByCategoryId(categoryId)

                .flatMap(service ->

                        categoryRepository.findById(
                                        service.getCategoryId()
                                )

                                .map(category ->
                                        mapToResponse(
                                                service,
                                                category
                                        )
                                )
                );
    }

    // =====================================================
    // UPDATE SERVICE
    // =====================================================
    @Override
    public Mono<ServiceResponse> update(
            Long id,
            ServiceRequest request
    ) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        )
                )

                .flatMap(existing ->

                        categoryRepository.findById(existing.getCategoryId())

                                .switchIfEmpty(
                                        Mono.error(
                                                new ResourceNotFoundException(
                                                        "Category not found"
                                                )
                                        )
                                )

                                .flatMap(category -> {

                                    // =========================
                                    // UPDATE BASIC FIELDS
                                    // =========================
                                    existing.setName(request.getName());
                                    existing.setDescription(request.getDescription());
                                    existing.setPrice(request.getPrice());
                                    existing.setDiscountedPrice(request.getDiscountedPrice());
                                    existing.setDurationMinutes(request.getDurationMinutes());
                                    existing.setFeatured(request.getFeatured());
                                    existing.setServiceType(request.getServiceType());
                                    existing.setUpdatedAt(LocalDateTime.now());
                                    existing.setTaxPercentage(
                                            request.getTaxPercentage()
                                    );
                                    // =========================
                                    // IMAGE UPDATE LOGIC
                                    // =========================
                                    Mono<String> imageMono;

                                    if (request.getFile() != null) {
                                        // NEW IMAGE UPLOAD
                                        imageMono = cloudinaryService.upload(request.getFile());
                                    } else {
                                        // KEEP OLD IMAGE
                                        imageMono = Mono.justOrEmpty(existing.getImageUrl());
                                    }

                                    return imageMono.flatMap(imageUrl -> {

                                        existing.setImageUrl(imageUrl);

                                        return repository.save(existing)
                                                .map(saved -> mapToResponse(saved, category));
                                    });
                                })
                );
    }

    // =====================================================
    // DELETE SERVICE
    // =====================================================

    @Override
    public Mono<Void> delete(
            Long id
    ) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        )
                )

                .flatMap(repository::delete);
    }


    public Flux<ServiceResponse> getByVendorId(Long vendorId) {

        return repository.findByVendorId(vendorId)

                .flatMap(service ->

                        categoryRepository.findById(
                                        service.getCategoryId()
                                )

                                .map(category ->
                                        mapToResponse(
                                                service,
                                                category
                                        )
                                )
                );
    }


    // =====================================================
    // MAPPER
    // =====================================================

    private ServiceResponse mapToResponse(
            Service service,
            Category category
    ) {

        Double discountedPrice =
                service.getDiscountedPrice() != null
                        ? service.getDiscountedPrice()
                        : service.getPrice();

        Double taxPercentage =
                service.getTaxPercentage() != null
                        ? service.getTaxPercentage()
                        : 18.0;

        Double taxAmount =
                discountedPrice * taxPercentage / 100;

        Double finalPrice =
                discountedPrice + taxAmount;
        return ServiceResponse.builder()
                .id(service.getId())
                .categoryId(service.getCategoryId())
                .categoryName(category.getName())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .discountedPrice(
                        service.getDiscountedPrice()
                )
                .taxPercentage(taxPercentage)
                .taxAmount(taxAmount)
                .finalPrice(finalPrice)
                .durationMinutes(
                        service.getDurationMinutes()
                )
                .imageUrl(service.getImageUrl())
                .featured(service.getFeatured())
                .active(service.getActive())
                .rating(service.getRating())
                .totalBookings(
                        service.getTotalBookings()
                )
                .serviceType(
                        service.getServiceType()
                )
                .createdAt(service.getCreatedAt())
                .build();
    }

    // =====================================================
    // GENERATE SERVICE CODE
    // =====================================================

    private String generateCode() {

        return "SRV-" +

                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}

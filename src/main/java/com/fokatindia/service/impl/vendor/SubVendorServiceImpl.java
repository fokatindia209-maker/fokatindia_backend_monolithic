package com.fokatindia.service.impl.vendor;


import com.fokatindia.dto.vendor.SubVendorRequest;
import com.fokatindia.dto.vendor.SubVendorResponse;
import com.fokatindia.entity.vendor.SubVendor;
import com.fokatindia.exception.ResourceNotFoundException;
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
                            .map(this::mapToResponse);
                });
    }

    @Override
    public Flux<SubVendorResponse> getSubVendors(
            Long vendorId
    ) {

        return subVendorRepository.findByVendorId(
                        vendorId
                )

                .map(this::mapToResponse);
    }

    @Override
    public Flux<SubVendorResponse> getSubVendorsWithUser(
            Long vendorId
    ) {

        return subVendorRepository.findSubVendorsWithUser(vendorId);
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

                            .map(this::mapToResponse);
                });
    }

    @Override
    public Flux<SubVendorResponse> getAllSubVendors() {
        return subVendorRepository.findAll()
                .map(this::mapToResponse);
    }

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    private SubVendorResponse mapToResponse(
            SubVendor subVendor
    ) {

        return  new  SubVendorResponse(
                subVendor.getUserId(),
                subVendor.getSubVendorId(),
                subVendor.getVendorId(),
                subVendor.getSpecialization(),
                subVendor.getExperienceYears(),
                subVendor.getAvailabilityStatus(),
                subVendor.getRating(),
                subVendor.getCreatedAt()
        );
    }




//    @Override
//    public Mono<DocumentResponse> uploadMyDocument(String documentType, FilePart file) {
//        return AuthUtils.getSubVendorId()
//                .flatMap(subVendorRepo::findById)
//                .switchIfEmpty(Mono.error(new RuntimeException("SubVendor not found")))
//                .flatMap(subVendor ->
//                     cloudinaryService.upload(file)
//                      .flatMap(url -> {
//
//                    Document d = new Document();
//                    d.setVendorId(subVendor.getVendorId());
//                    d.setDocumentType(documentType);
//                    d.setDocumentUrl(url);
//                    d.setStatus("PENDING");
//                    d.setUploadedAt(LocalDateTime.now());
//                    return documentRepo.save(d);
//                }))
//                .map(doc -> new DocumentResponse(
//                        doc.getDocumentId(),
//                        doc.getVendorId(),
//                        doc.getDocumentType(),
//                        doc.getDocumentUrl(),
//                        doc.getStatus()
//                ));
//    }
//
//
//    @Override
//    public Flux<DocumentResponse> getMyDocuments() {
//        return AuthUtils.getSubVendorId()
//                .flatMapMany(documentRepo::findByVendorId   // or findBySubVendorId if you store it
//                )
//                .map(doc -> new DocumentResponse(
//                        doc.getDocumentId(),
//                        doc.getVendorId(),
//                        doc.getDocumentType(),
//                        doc.getDocumentUrl(),
//                        doc.getStatus()
//                ));
//    }

}

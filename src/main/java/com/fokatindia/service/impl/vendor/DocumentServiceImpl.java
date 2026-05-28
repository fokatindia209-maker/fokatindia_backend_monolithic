package com.fokatindia.service.impl.vendor;


import com.fokatindia.dto.vendor.DocumentResponse;
import com.fokatindia.dto.vendor.DocumentUploadRequest;
import com.fokatindia.entity.vendor.Document;
import com.fokatindia.exception.ResourceNotFoundException;
import com.fokatindia.repository.vendor.DocumentRepository;
import com.fokatindia.service.CloudinaryService;
import com.fokatindia.service.vendor.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepo;

    private final CloudinaryService cloudinaryService;

    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @Override
    public Mono<DocumentResponse> uploadDocument(
            DocumentUploadRequest request
    ) {

        return cloudinaryService.upload(
                        request.getFile()
                )

                .flatMap(fileUrl -> {

                    Document document =
                            new Document();

                    document.setUserId(
                            request.getUserId()
                    );

                    document.setDocumentType(
                            request.getDocumentType()
                    );

                    document.setDocumentUrl(
                            fileUrl
                    );

                    document.setStatus(
                            "PENDING"
                    );

                    document.setUploadedAt(
                            LocalDateTime.now()
                    );

                    return documentRepo.save(
                                    document
                            )

                            .map(this::mapResponse);
                });
    }

    // =====================================================
    // VERIFY DOCUMENT
    // =====================================================



    @Override
    public Mono<DocumentResponse> verifyDocument(
            Long documentId,
            String status
    ) {

        return documentRepo.findById(documentId)

                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Document not found"
                                )
                        )
                )

                .flatMap(document -> {

                    document.setStatus(status);

                    document.setVerifiedAt(
                            LocalDateTime.now()
                    );

                    return documentRepo.save(
                                    document
                            )

                            .map(this::mapResponse);
                });
    }

    @Override
    public Mono<String> getDocumentStatus(Long userId) {

        return documentRepo.findByUserId(userId)

                .collectList()

                .map(documents -> {

                    if (documents.isEmpty()) {
                        return "PENDING";
                    }

                    boolean allApproved = documents.stream()
                            .allMatch(doc ->
                                    "APPROVED".equalsIgnoreCase(doc.getStatus())
                            );

                    return allApproved
                            ? "APPROVED"
                            : "PENDING";
                });
    }

    // =====================================================
    // GET USER DOCUMENTS
    // =====================================================

    @Override
    public Flux<DocumentResponse> getUserDocuments(
            Long userId
    ) {

        return documentRepo.findByUserId(userId)
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(
                                        "Documents not found"
                                )
                        )
                )
                .map(this::mapResponse);
    }

    // =====================================================
    // MAPPER
    // =====================================================

    private DocumentResponse mapResponse(
            Document document
    ) {
        return  new DocumentResponse(
                        document.getDocumentId(),
                (long) Integer.parseInt(String.valueOf(document.getUserId())),
                        document.getDocumentType(),
                        document.getDocumentUrl(),
                        document.getStatus(),
                        document.getRemarks(),
                        document.getUploadedAt(),
                        document.getVerifiedAt()
        );
    }


    @Override
    public Flux<DocumentResponse> getAllDocuments() {
        return documentRepo.findAll()
                .map(this::mapResponse);
    }



}
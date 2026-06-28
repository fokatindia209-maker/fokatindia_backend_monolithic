package com.fokatindia.service.vendor;


import com.fokatindia.dto.vendor.DocumentResponse;
import com.fokatindia.dto.vendor.DocumentUploadRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DocumentService {
    Mono<DocumentResponse> uploadDocument(DocumentUploadRequest request);
    Mono<DocumentResponse> verifyDocument(Long id, String status, String remarks);
    Flux<DocumentResponse> getUserDocuments(Long userId);

    Mono<String> getDocumentStatus(Long userId);
    Flux<DocumentResponse> getAllDocuments();
}

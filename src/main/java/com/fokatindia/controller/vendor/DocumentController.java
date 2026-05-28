package com.fokatindia.controller.vendor;



import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.vendor.DocumentResponse;
import com.fokatindia.dto.vendor.DocumentUploadRequest;
import com.fokatindia.service.vendor.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @PreAuthorize("hasAuthority('DOCUMENT_UPLOAD')")
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Mono<ApiResponse<DocumentResponse>> upload(

            @RequestPart("userId") String userId,

            @RequestPart("documentType") String documentType,

            @RequestPart("file") FilePart file
    ) {

        DocumentUploadRequest request =
                new DocumentUploadRequest();

        request.setUserId(Long.valueOf(userId));
        request.setDocumentType(documentType);
        request.setFile(file);

        return service.uploadDocument(request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Document uploaded successfully",
                                res
                        )
                );
    }

    // =====================================================
    // VERIFY DOCUMENT (ADMIN)
    // =====================================================

    @PreAuthorize("hasAuthority('VENDOR_MANAGE')")
    @PutMapping("/{id}/verify")
    public Mono<ApiResponse<DocumentResponse>> verify(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        return service.verifyDocument(id, status)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Document verified successfully",
                                res
                        )
                );
    }


    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @GetMapping("/{userId}")
    public Mono<ApiResponse<List<DocumentResponse>>>getVendor(
            @PathVariable Long userId
    ) {

        return service.getUserDocuments(userId)
                .collectList()
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Document details fetched",
                                res
                        )
                );
    }


    // =====================================================
    // GET ALL VENDORS (ADMIN)
    // =====================================================

    @PreAuthorize("hasAuthority('DOCUMENT_MANAGE')")
    @GetMapping
    public Mono<ApiResponse<List<DocumentResponse>>> getAllDocuments() {

        return service.getAllDocuments()
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Vendor list fetched",
                                list
                        )
                );
    }



}
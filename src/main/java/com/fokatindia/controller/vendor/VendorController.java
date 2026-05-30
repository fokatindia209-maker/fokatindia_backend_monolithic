package com.fokatindia.controller.vendor;


import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.vendor.VendorRequest;
import com.fokatindia.dto.vendor.VendorResponse;
import com.fokatindia.service.vendor.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService service;




    // =====================================================
    // CREATE VENDOR
    // =====================================================

    @PreAuthorize("hasAuthority('VENDOR_CREATE')")
    @PostMapping
    public Mono<ApiResponse<VendorResponse>> createVendor(
            @RequestBody VendorRequest request
    ) {

        return service.createVendor(request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                201,
                                "Vendor profile created",
                                res
                        )
                );
    }

    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @GetMapping("/{id}")
    public Mono<ApiResponse<VendorResponse>> getVendor(
            @PathVariable Long id
    ) {

        return service.getVendor(id)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Vendor details fetched",
                                res
                        )
                );
    }

    // =====================================================
    // GET ALL VENDORS (ADMIN)
    // =====================================================

    @PreAuthorize("hasAuthority('VENDOR_MANAGE')")
    @GetMapping("")
    public Mono<ApiResponse<List<VendorResponse>>> getAllVendors() {

        return service.getAllVendors()
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

    // =====================================================
    // UPDATE VENDOR PROFILE
    // =====================================================

    @PreAuthorize("hasAuthority('PROFILE_UPDATE')")
    @PutMapping("/{id}")
    public Mono<ApiResponse<VendorResponse>> updateVendor(
            @PathVariable Long id,
            @RequestBody VendorRequest request
    ) {

        return service.updateVendor(id, request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Vendor updated successfully",
                                res
                        )
                );
    }


    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @GetMapping("/users/{userId}")
    public Mono<ApiResponse<VendorResponse>> getVendorByUserId(
            @PathVariable Long userId
    ) {

        return service.getVendorByUserId(userId)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Vendor details fetched",
                                res
                        )
                );
    }
}
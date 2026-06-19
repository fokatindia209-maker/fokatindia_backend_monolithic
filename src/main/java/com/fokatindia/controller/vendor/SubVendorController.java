package com.fokatindia.controller.vendor;

import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.vendor.SubVendorRequest;
import com.fokatindia.dto.vendor.SubVendorResponse;
import com.fokatindia.service.vendor.SubVendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/subvendors")
@RequiredArgsConstructor
public class SubVendorController {

    private final SubVendorService service;

    // =====================================================
    // CREATE SUBVENDOR
    // =====================================================

    @PreAuthorize("hasAuthority('SUBVENDOR_CREATE')")
    @PostMapping
    public Mono<ApiResponse<SubVendorResponse>> create(
            @RequestBody SubVendorRequest request
    ) {

        return service.addSubVendor(request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "SubVendor created successfully",
                                res
                        )
                );
    }

    // =====================================================
    // GET SUBVENDORS BY VENDOR
    // =====================================================

    @PreAuthorize("hasAuthority('SUBVENDOR_VIEW')")
    @GetMapping("/vendor/{vendorId}")
    public Mono<ApiResponse<List<SubVendorResponse>>> getByVendor(
            @PathVariable Long vendorId
    ) {

        return service.getSubVendors(vendorId)
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "SubVendor list fetched",
                                list
                        )
                );
    }



    @GetMapping("/service/{serviceId}")
    public Mono<ApiResponse<List<SubVendorResponse>>> getSubVendorByServiceId(
            @PathVariable Long serviceId
    ) {

        return service.getSubVendorByServiceId(serviceId)
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "SubVendor list fetched",
                                list
                        )
                );
    }


    @PreAuthorize("hasAuthority('SUBVENDOR_VIEW')")
    @GetMapping("/vendor/{vendorId}/users")
    public Mono<ApiResponse<List<SubVendorResponse>>> getSubVendorsWithUser(
            @PathVariable Long vendorId
    ) {

        return service.getSubVendorsWithUser(vendorId)
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "SubVendor list fetched",
                                list
                        )
                );
    }

    // =====================================================
    // UPDATE SUBVENDOR
    // =====================================================

    @PreAuthorize("hasAuthority('PROFILE_UPDATE')")
    @PutMapping("/{id}")
    public Mono<ApiResponse<SubVendorResponse>> update(
            @PathVariable Long id,
            @RequestBody SubVendorRequest request
    ) {

        return service.updateSubVendor(id, request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "SubVendor updated successfully",
                                res
                        )
                );
    }


    @PreAuthorize("hasAuthority('SUBVENDOR_MANAGE')")
    @GetMapping
    public Mono<ApiResponse<List<SubVendorResponse>>> getAllSubVendors() {

        return service.getAllSubVendors()
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Sub Vendor list fetched",
                                list
                        )
                );
    }
}

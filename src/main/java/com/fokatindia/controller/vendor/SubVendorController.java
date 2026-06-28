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
            @PathVariable Long serviceId,
            @RequestParam Double lat,
            @RequestParam Double lng
    ) {

        return service.getSubVendorByServiceId(serviceId, lat, lng)
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


    @GetMapping("/{subVendorId}")
    public Mono<ApiResponse<SubVendorResponse>> getSubVendorBySubVendorId(
            @PathVariable Long subVendorId
    ) {
        return service.getSubVendorBySubVendorId(subVendorId)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "SubVendor fetched successfully",
                                res
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


    // =====================================================
    // DEACTIVATE SUBVENDOR
    // =====================================================

    @PreAuthorize("hasAuthority('SUBVENDOR_CREATE')")
    @PutMapping("/{id}/deactivate")
    public Mono<ApiResponse<SubVendorResponse>> deactivate(
            @PathVariable Long id
    ) {
        return service.getSubVendorBySubVendorId(id)
                .flatMap(existing -> {
                    SubVendorRequest req = new SubVendorRequest();
                    req.setUserId(existing.getUserId());
                    req.setVendorId(existing.getVendorId());
                    req.setSpecialization(existing.getSpecialization());
                    req.setLatitude(existing.getLatitude());
                    req.setLongitude(existing.getLongitude());
                    req.setServiceRadiusKm(existing.getServiceRadiusKm());
                    req.setExperienceYears(existing.getExperienceYears());
                    req.setAvailabilityStatus("INACTIVE");
                    req.setRating(existing.getRating());
                    return service.updateSubVendor(id, req);
                })
                .map(res -> new ApiResponse<>(
                        "success", 200, "SubVendor deactivated successfully", res
                ));
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

    // =====================================================
    // DELETE SUBVENDOR
    // =====================================================

    @PreAuthorize("hasAuthority('SUBVENDOR_MANAGE')")
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> deleteSubVendor(@PathVariable Long id) {
        return service.deleteSubVendor(id)
                .thenReturn(new ApiResponse<>("success", 200, "SubVendor deleted successfully", "Deleted"));
    }
}

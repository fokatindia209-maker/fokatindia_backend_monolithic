package com.fokatindia.controller;

import com.fokatindia.dto.AddressRequest;
import com.fokatindia.dto.AddressResponse;
import com.fokatindia.dto.ApiResponse;
import com.fokatindia.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restful/v1/api/addresses")
public class AddressController {

    private final AddressService addressService;

    // ==========================================
    // CREATE ADDRESS
    // ==========================================
    @PostMapping
    @PreAuthorize("hasAuthority('ADDRESS_CREATE')")
    public Mono<ApiResponse<AddressResponse>> saveAddress(
            @RequestBody AddressRequest request
    ) {
        return addressService.saveAddress(request)
                .map(response ->
                        new ApiResponse<>(
                                "success",
                                201,
                                "Address created successfully",
                                response
                        )
                );
    }

    // ==========================================
    // UPDATE ADDRESS
    // ==========================================
    @PutMapping("/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_UPDATE')")
    public Mono<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequest request
    ) {
        return addressService.updateAddress(addressId, request)
                .map(response ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Address updated successfully",
                                response
                        )
                );
    }

    // ==========================================
    // GET ADDRESS BY ID
    // ==========================================
    @GetMapping("/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_VIEW')")
    public Mono<ApiResponse<AddressResponse>> getAddress(
            @PathVariable Long addressId
    ) {
        return addressService.getAddress(addressId)
                .map(response ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Address fetched successfully",
                                response
                        )
                );
    }

    // ==========================================
    // GET USER ADDRESSES
    // ==========================================
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADDRESS_VIEW')")
    public Mono<ApiResponse<List<AddressResponse>>> getUserAddresses(
            @PathVariable Long userId
    ) {
        return addressService.getUserAddresses(userId)
                .collectList()
                .map(response ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Address list fetched successfully",
                                response
                        )
                );
    }

    // ==========================================
    // GET DEFAULT ADDRESS
    // ==========================================
    @GetMapping("/user/{userId}/default")
    @PreAuthorize("hasAuthority('ADDRESS_VIEW')")
    public Mono<ApiResponse<AddressResponse>> getDefaultAddress(
            @PathVariable Long userId
    ) {
        return addressService.getDefaultAddress(userId)
                .map(response ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Default address fetched successfully",
                                response
                        )
                );
    }

    // ==========================================
    // SET DEFAULT ADDRESS
    // ==========================================
    @PutMapping("/{addressId}/default")
    @PreAuthorize("hasAuthority('ADDRESS_SET_DEFAULT')")
    public Mono<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable Long addressId
    ) {
        return addressService.setDefaultAddress(addressId)
                .map(response ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Default address updated successfully",
                                response
                        )
                );
    }

    // ==========================================
    // DELETE ADDRESS
    // ==========================================
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_DELETE')")
    public Mono<ApiResponse<Void>> deleteAddress(
            @PathVariable Long addressId
    ) {
        return addressService.deleteAddress(addressId)
                .thenReturn(
                        new ApiResponse<>(
                                "success",
                                200,
                                "Address deleted successfully",
                                null
                        )
                );
    }
}
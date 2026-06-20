package com.fokatindia.controller;



import com.fokatindia.dto.AddressRequest;
import com.fokatindia.dto.AddressResponse;
import com.fokatindia.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restful/v1/api/addresses")
public class AddressController {

    private final AddressService addressService;


    // ==========================================
    // CREATE ADDRESS
    // POST /restful/v1/api/addresses
    // ==========================================
    @PostMapping
    @PreAuthorize("hasAuthority('ADDRESS_CREATE')")
    public Mono<AddressResponse> saveAddress(
            @RequestBody AddressRequest request
    ) {
        return addressService.saveAddress(request);
    }


    // ==========================================
    // UPDATE ADDRESS
    // PUT /restful/v1/api/addresses/{addressId}
    // ==========================================
    @PutMapping("/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_UPDATE')")
    public Mono<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequest request
    ) {
        return addressService.updateAddress(
                addressId,
                request
        );
    }


    // ==========================================
    // GET ADDRESS BY ID
    // GET /restful/v1/api/addresses/{addressId}
    // ==========================================
    @GetMapping("/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_VIEW')")
    public Mono<AddressResponse> getAddress(
            @PathVariable Long addressId
    ) {
        return addressService.getAddress(addressId);
    }

    // ==========================================
    // GET ALL USER ADDRESSES
    // GET /restful/v1/api/addresses/user/{userId}
    // ==========================================
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADDRESS_VIEW')")
    public Flux<AddressResponse> getUserAddresses(
            @PathVariable Long userId
    ) {
        return addressService.getUserAddresses(userId);
    }

    // ==========================================
    // GET DEFAULT ADDRESS
    // GET /restful/v1/api/addresses/user/{userId}/default
    // ==========================================
    @GetMapping("/user/{userId}/default")
    @PreAuthorize("hasAuthority('ADDRESS_VIEW')")
    public Mono<AddressResponse> getDefaultAddress(
            @PathVariable Long userId
    ) {
        return addressService.getDefaultAddress(userId);
    }

    // ==========================================
    // SET DEFAULT ADDRESS
    // PUT /restful/v1/api/addresses/{addressId}/default
    // ==========================================
    @PutMapping("/{addressId}/default")
    @PreAuthorize("hasAuthority('ADDRESS_SET_DEFAULT')")
    public Mono<AddressResponse> setDefaultAddress(
            @PathVariable Long addressId
    ) {
        return addressService.setDefaultAddress(addressId);
    }




    //delete by addressId
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_DELETE')")
    public Mono<Void> deleteAddress(
            @PathVariable Long addressId
    ) {
        return addressService.deleteAddress(addressId);
    }
}
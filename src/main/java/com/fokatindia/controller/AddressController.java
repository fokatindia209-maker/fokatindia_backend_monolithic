package com.fokatindia.controller;



import com.fokatindia.dto.AddressRequest;
import com.fokatindia.dto.AddressResponse;
import com.fokatindia.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restful/v1/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public Mono<AddressResponse> saveAddress(
            @RequestBody AddressRequest request
    ) {
        return addressService.saveAddress(request);
    }

    @PutMapping("/{addressId}")
    public Mono<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequest request
    ) {
        return addressService.updateAddress(
                addressId,
                request
        );
    }

    @GetMapping("/{addressId}")
    public Mono<AddressResponse> getAddress(
            @PathVariable Long addressId
    ) {
        return addressService.getAddress(addressId);
    }

    @GetMapping("/user/{userId}")
    public Flux<AddressResponse> getUserAddresses(
            @PathVariable Long userId
    ) {
        return addressService.getUserAddresses(userId);
    }

    @DeleteMapping("/{addressId}")
    public Mono<Void> deleteAddress(
            @PathVariable Long addressId
    ) {
        return addressService.deleteAddress(addressId);
    }
}
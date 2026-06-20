package com.fokatindia.service;

import com.fokatindia.dto.AddressRequest;
import com.fokatindia.dto.AddressResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  AddressService {

    Mono<AddressResponse> saveAddress(AddressRequest request);

    Mono<AddressResponse> updateAddress(
            Long addressId,
            AddressRequest request
    );

    Mono<AddressResponse> getAddress(Long addressId);

    Flux<AddressResponse> getUserAddresses(Long userId);

    Mono<Void> deleteAddress(Long addressId);
}

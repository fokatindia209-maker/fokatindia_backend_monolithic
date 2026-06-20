package com.fokatindia.service.impl;



import com.fokatindia.dto.AddressRequest;
import com.fokatindia.dto.AddressResponse;
import com.fokatindia.entity.Address;
import com.fokatindia.repository.AddressRepository;
import com.fokatindia.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Mono<AddressResponse> saveAddress(
            AddressRequest request
    ) {

        Address address = new Address();

        address.setUserId(request.getUserId());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setIsDefault(request.getIsDefault());
        address.setCreatedAt(LocalDateTime.now());

        return addressRepository
                .save(address)
                .map(this::mapResponse);
    }

    @Override
    public Mono<AddressResponse> updateAddress(
            Long addressId,
            AddressRequest request
    ) {

        return addressRepository
                .findById(addressId)
                .flatMap(address -> {

                    address.setAddressLine1(request.getAddressLine1());
                    address.setAddressLine2(request.getAddressLine2());
                    address.setCity(request.getCity());
                    address.setState(request.getState());
                    address.setPincode(request.getPincode());
                    address.setCountry(request.getCountry());
                    address.setLatitude(request.getLatitude());
                    address.setLongitude(request.getLongitude());
                    address.setIsDefault(request.getIsDefault());

                    return addressRepository.save(address);
                })
                .map(this::mapResponse);
    }

    @Override
    public Mono<AddressResponse> getAddress(Long addressId) {

        return addressRepository
                .findById(addressId)
                .map(this::mapResponse);
    }

    @Override
    public Flux<AddressResponse> getUserAddresses(Long userId) {

        return addressRepository
                .findByUserId(userId)
                .map(this::mapResponse);
    }

    @Override
    public Mono<Void> deleteAddress(Long addressId) {

        return addressRepository.deleteById(addressId);
    }

    private AddressResponse mapResponse(Address address) {

        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .userId(address.getUserId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .country(address.getCountry())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .build();
    }
}
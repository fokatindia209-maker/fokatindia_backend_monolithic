package com.fokatindia.service.vendor;

import com.fokatindia.dto.vendor.ServiceRequest;
import com.fokatindia.dto.vendor.ServiceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceService {
    Mono<ServiceResponse> create(ServiceRequest request);

    Mono<ServiceResponse> getById(Long id);

    Flux<ServiceResponse> getAll();

    Flux<ServiceResponse> getByCategory(Long categoryId);

    Mono<ServiceResponse> update(Long id, ServiceRequest request);

    Mono<Void> delete(Long id);
    Flux<ServiceResponse> getByVendorId(Long vendorId);
    Flux<ServiceResponse> getBySubVendorId(Long subVendorId);

}

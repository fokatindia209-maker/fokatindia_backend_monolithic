package com.fokatindia.service.vendor;

import com.fokatindia.dto.vendor.SubVendorRequest;
import com.fokatindia.dto.vendor.SubVendorResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubVendorService {
    Mono<SubVendorResponse> addSubVendor(SubVendorRequest request);
    Flux<SubVendorResponse> getSubVendors(Long vendorId);
    Mono<SubVendorResponse> updateSubVendor(Long id, SubVendorRequest request);
    Flux<SubVendorResponse> getAllSubVendors();
    Flux<SubVendorResponse> getSubVendorsWithUser(Long vendorId);
    Flux<SubVendorResponse> getSubVendorByServiceId(Long serviceId);


}

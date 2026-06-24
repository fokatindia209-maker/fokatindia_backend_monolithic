package com.fokatindia.service.vendor;

import com.fokatindia.dto.vendor.CategoryRequest;
import com.fokatindia.dto.vendor.CategoryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    Mono<CategoryResponse> create(CategoryRequest request);

    Mono<CategoryResponse> getById(Long id);

    Flux<CategoryResponse> getAll();

    Mono<CategoryResponse> update(
            Long id,
            CategoryRequest request
    );

    Mono<Void> delete(Long id);

    Flux<CategoryResponse> getByVendorId(Long vendorId);


}

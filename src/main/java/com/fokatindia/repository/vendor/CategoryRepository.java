package com.fokatindia.repository.vendor;


import com.fokatindia.entity.vendor.Category;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryRepository
        extends ReactiveCrudRepository<Category, Long> {
    Mono<Category> findByName(String name);

    Mono<Category> findBySlug(String slug);

    @Query("""
        SELECT c.*
        FROM categories c
        JOIN vendor_categories vc
        ON vc.category_id = c.id
        WHERE vc.vendor_id = :vendorId
        AND vc.active = true
    """)
    Flux<Category> findByVendorId(Long vendorId);
}
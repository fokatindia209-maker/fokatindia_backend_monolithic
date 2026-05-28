package com.fokatindia.repository.vendor;


import com.fokatindia.entity.vendor.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CategoryRepository
        extends ReactiveCrudRepository<Category, Long> {
    Mono<Category> findByName(String name);

    Mono<Category> findBySlug(String slug);
}
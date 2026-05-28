package com.fokatindia.repository.vendor;

import com.fokatindia.entity.vendor.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DocumentRepository extends ReactiveCrudRepository<Document, Long> {
        Flux<Document> findByUserId(Long userId);
}
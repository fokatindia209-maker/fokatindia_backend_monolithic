package com.fokatindia.repository;

import com.fokatindia.entity.Permission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PermissionRepository extends ReactiveCrudRepository<Permission, Long> {
    @Query("""
        SELECT p.name
        FROM permissions p
        JOIN role_permissions rp
            ON rp.permission_id = p.permission_id
        JOIN user_roles ur
            ON ur.role_id = rp.role_id
        WHERE ur.user_id = :userId
    """)
    Flux<String> findPermissionsByUserId(Long userId);

    Mono<Permission> findByName(String name);
}
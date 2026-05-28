package com.fokatindia.repository;

import com.fokatindia.entity.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {

    // FIND ROLE BY NAME
    Mono<Role> findByName(String name);

    @Query("""
    SELECT r.name
    FROM roles r
    JOIN user_roles ur
        ON ur.role_id = r.role_id
    WHERE ur.user_id = :userId
""")
    Mono<String> findRoleNameByUserId(Long userId);

    @Query("""
SELECT p.name
FROM permissions p
JOIN role_permissions rp ON p.permission_id = rp.permission_id
JOIN user_roles ur ON ur.role_id = rp.role_id
WHERE ur.user_id = :userId
""")
    Flux<String> findPermissionsByUserId(Long userId);


}
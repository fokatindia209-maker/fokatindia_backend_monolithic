package com.fokatindia.repository;

import com.fokatindia.entity.RolePermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RolePermissionRepository extends ReactiveCrudRepository<RolePermission, Long> {

    Flux<RolePermission> findByRoleId(Long roleId);

    Flux<RolePermission> findByPermissionId(Long permissionId);
}
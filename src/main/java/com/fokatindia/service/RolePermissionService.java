package com.fokatindia.service;


import com.fokatindia.dto.RolePermissionRequest;
import com.fokatindia.dto.RolePermissionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolePermissionService {
    Mono<RolePermissionResponse> assignPermission(RolePermissionRequest request);


    Flux<RolePermissionResponse> getAllRolePermissions();

    Mono<RolePermissionResponse> getById(Long id);

    Flux<RolePermissionResponse> getByRoleId(Long roleId);

    Mono<RolePermissionResponse> update(
            Long id,
            RolePermissionRequest request
    );

    Mono<Void> delete(Long id);
}

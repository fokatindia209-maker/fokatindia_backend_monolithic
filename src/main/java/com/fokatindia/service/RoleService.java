package com.fokatindia.service;

import com.fokatindia.dto.RoleRequest;
import com.fokatindia.dto.RoleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleService {

    // CREATE ROLE

    Mono<RoleResponse> createRole(
            RoleRequest request
    );

    // GET ALL ROLES

    Flux<RoleResponse> getAllRoles();

    // GET ROLE BY ID

    Mono<RoleResponse> getRoleById(
            Long roleId
    );

    // UPDATE ROLE

    Mono<RoleResponse> updateRole(
            Long roleId,
            RoleRequest request
    );

    // DELETE ROLE

    Mono<Void> deleteRole(
            Long roleId
    );
}

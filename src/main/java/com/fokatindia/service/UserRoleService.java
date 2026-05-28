package com.fokatindia.service;


import com.fokatindia.dto.UserRoleRequest;
import com.fokatindia.dto.UserRoleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRoleService {

    // ASSIGN ROLE
    Mono<UserRoleResponse> assignRole(UserRoleRequest request);



    // GET ALL

    Flux<UserRoleResponse> getAllUserRoles();

    // GET BY ID

    Mono<UserRoleResponse> getUserRoleById(
            Long id
    );

    // GET BY USER ID

    Flux<UserRoleResponse> getRolesByUserId(
            Long userId
    );

    // UPDATE

    Mono<UserRoleResponse> updateUserRole(
            Long id,
            UserRoleRequest request
    );

    // DELETE

    Mono<Void> removeRole(Long id);
}

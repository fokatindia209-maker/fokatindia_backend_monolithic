package com.fokatindia.controller;


import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.UserRoleRequest;
import com.fokatindia.dto.UserRoleResponse;
import com.fokatindia.service.UserRoleService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(
            UserRoleService userRoleService
    ) {
        this.userRoleService = userRoleService;
    }

    // ============================================
    // ASSIGN ROLE TO USER
    // POST:
    // /restful/v1/api/user-roles
    // ============================================

    @PostMapping
    public Mono<ApiResponse<UserRoleResponse>> assignRole(
            @RequestBody UserRoleRequest ur
    ) {

        return userRoleService
                .assignRole(ur)

                .map(r ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Role assigned to user",
                                r
                        )
                );
    }

    // ============================================
    // GET ALL USER ROLES
    // GET:
    // /restful/v1/api/user-roles
    // ============================================

    @GetMapping
    public Mono<ApiResponse<List<UserRoleResponse>>> getAllUserRoles() {
            return userRoleService
                    .getAllUserRoles()

                    .collectList()

                    .map(roles ->
                            new ApiResponse<>(
                                    "success",
                                    200,
                                    "User roles fetched",
                                    roles
                            )
                    );

    }

    // ============================================
    // GET USER ROLE BY ID
    // GET:
    // /restful/v1/api/user-roles/{id}
    // ============================================

    @GetMapping("/{id}")
    public Mono<ApiResponse<UserRoleResponse>> getUserRoleById(
            @PathVariable Long id
    ) {

        return userRoleService
                .getUserRoleById(id)

                .map(r ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "User role fetched",
                                r
                        )
                );
    }

    // ============================================
    // GET ROLES BY USER ID
    // GET:
    // /restful/v1/api/user-roles/user/{userId}
    // ============================================

    @GetMapping("/user/{userId}")
    public Mono<ApiResponse<Flux<UserRoleResponse>>> getRolesByUserId(
            @PathVariable Long userId
    ) {

        Flux<UserRoleResponse> roles =
                userRoleService.getRolesByUserId(userId);

        return Mono.just(
                new ApiResponse<>(
                        "success",
                        200,
                        "Roles fetched by user",
                        roles
                )
        );
    }

    // ============================================
    // UPDATE USER ROLE
    // PUT:
    // /restful/v1/api/user-roles/{id}
    // ============================================

    @PutMapping("/{id}")
    public Mono<ApiResponse<UserRoleResponse>> updateUserRole(
            @PathVariable Long id,
            @RequestBody UserRoleRequest ur
    ) {

        return userRoleService
                .updateUserRole(id, ur)

                .map(r ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "User role updated",
                                r
                        )
                );
    }

    // ============================================
    // REMOVE ROLE FROM USER
    // DELETE:
    // /restful/v1/api/user-roles/{id}
    // ============================================

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> removeRole(
            @PathVariable Long id
    ) {

        return userRoleService
                .removeRole(id)

                .then(
                        Mono.just(
                                new ApiResponse<>(
                                        "success",
                                        200,
                                        "Role removed successfully",
                                        "Deleted"
                                )
                        )
                );
    }
}
//package com.fokatindia.user_service.controller;
//
//import com.fokatindia.user_service.dto.ApiResponse;
//import com.fokatindia.user_service.dto.UserRoleRequest;
//import com.fokatindia.user_service.dto.UserRoleResponse;
//import com.fokatindia.user_service.service.UserRoleService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/restful/v1/api/user-roles")
//public class UserRoleController {
//
//    private final UserRoleService userRoleService;
//
//    public UserRoleController(UserRoleService userRoleService) { this.userRoleService = userRoleService; }
//
//    @PostMapping
//    public Mono<ApiResponse<UserRoleResponse>> assignRole(@RequestBody UserRoleRequest ur) {
//        return userRoleService.assignRole(ur)
//                .map(r -> new ApiResponse<>("success", 200, "Role assigned to user", r));
//    }
//}
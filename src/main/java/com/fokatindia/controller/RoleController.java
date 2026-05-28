package com.fokatindia.controller;

import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.RoleRequest;
import com.fokatindia.dto.RoleResponse;
import com.fokatindia.service.RoleService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(
            RoleService roleService
    ) {
        this.roleService = roleService;
    }

    // ============================================
    // CREATE ROLE
    // POST:
    // /restful/v1/api/roles
    // ============================================

    @PostMapping
    public Mono<ApiResponse<RoleResponse>>
    createRole(
            @RequestBody RoleRequest role
    ) {

        return roleService
                .createRole(role)

                .map(r ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Role created",
                                r
                        )
                );
    }

    // ============================================
    // GET ALL ROLES
    // GET:
    // /restful/v1/api/roles
    // ============================================

    @GetMapping
    public Mono<ApiResponse<List<RoleResponse>>>
    getAllRoles() {

        return roleService
                .getAllRoles()

                .collectList()

                .map(roles ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Roles fetched",
                                roles
                        )
                );
    }

    // ============================================
    // GET ROLE BY ID
    // GET:
    // /restful/v1/api/roles/{id}
    // ============================================

    @GetMapping("/{roleId}")
    public Mono<ApiResponse<RoleResponse>>
    getRoleById(
            @PathVariable Long roleId
    ) {

        return roleService
                .getRoleById(roleId)

                .map(role ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Role fetched",
                                role
                        )
                );
    }

    // ============================================
    // UPDATE ROLE
    // PUT:
    // /restful/v1/api/roles/{id}
    // ============================================

    @PutMapping("/{roleId}")
    public Mono<ApiResponse<RoleResponse>>
    updateRole(
            @PathVariable Long roleId,
            @RequestBody RoleRequest role
    ) {

        return roleService
                .updateRole(roleId, role)

                .map(updated ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Role updated",
                                updated
                        )
                );
    }

    // ============================================
    // DELETE ROLE
    // DELETE:
    // /restful/v1/api/roles/{id}
    // ============================================

    @DeleteMapping("/{roleId}")
    public Mono<ApiResponse<String>>
    deleteRole(
            @PathVariable Long roleId
    ) {

        return roleService
                .deleteRole(roleId)

                .then(
                        Mono.just(
                                new ApiResponse<>(
                                        "success",
                                        200,
                                        "Role deleted successfully",
                                        "Deleted"
                                )
                        )
                );
    }
}
//package com.fokatindia.user_service.controller;
//
//import com.fokatindia.user_service.dto.ApiResponse;
//import com.fokatindia.user_service.dto.RoleRequest;
//import com.fokatindia.user_service.dto.RoleResponse;
//import com.fokatindia.user_service.entity.Role;
//import com.fokatindia.user_service.service.RoleService;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/restful/v1/api/roles")
//public class RoleController {
//
//    private final RoleService roleService;
//
//    public RoleController(RoleService roleService) { this.roleService = roleService; }
//
//    @PostMapping
//    public Mono<ApiResponse<RoleResponse>> createRole(@RequestBody RoleRequest role) {
//        return roleService.createRole(role)
//                .map(r -> new ApiResponse<>("success", 200, "Role created", r));
//    }
//
//    @GetMapping
//    public Mono<ApiResponse<List<RoleResponse>>> getAllRoles() {
//        return roleService.getAllRoles()
//                .collectList()
//                .map(roles -> new ApiResponse<>("success", 200, "Roles fetched", roles));
//    }
//}
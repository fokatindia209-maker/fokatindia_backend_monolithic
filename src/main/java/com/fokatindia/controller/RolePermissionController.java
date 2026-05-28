package com.fokatindia.controller;

import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.RolePermissionRequest;
import com.fokatindia.dto.RolePermissionResponse;
import com.fokatindia.service.RolePermissionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/role-permissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(
            RolePermissionService rolePermissionService
    ) {
        this.rolePermissionService = rolePermissionService;
    }

    // ============================================
    // ASSIGN PERMISSION TO ROLE
    // POST
    // ============================================

    @PostMapping
    public Mono<ApiResponse<RolePermissionResponse>>
    assignPermission(@RequestBody RolePermissionRequest rp) {

        return rolePermissionService
                .assignPermission(rp)

                .map(r ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Permission assigned to role",
                                r
                        )
                );
    }

    // ============================================
    // GET ALL ROLE-PERMISSIONS
    // ============================================

    @GetMapping
    public Mono<ApiResponse<List<RolePermissionResponse>>>
    getAllRolePermissions() {

        return rolePermissionService
                .getAllRolePermissions()

                .collectList()

                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Role permissions fetched",
                                list
                        )
                );
    }

    // ============================================
    // GET BY ID
    // ============================================

    @GetMapping("/{id}")
    public Mono<ApiResponse<RolePermissionResponse>>
    getById(@PathVariable Long id) {

        return rolePermissionService
                .getById(id)

                .map(r ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Role permission fetched",
                                r
                        )
                );
    }

    // ============================================
    // GET PERMISSIONS BY ROLE ID
    // ============================================

    @GetMapping("/role/{roleId}")
    public Mono<ApiResponse<List<RolePermissionResponse>>>
    getByRoleId(@PathVariable Long roleId) {

        return rolePermissionService
                .getByRoleId(roleId)

                .collectList()

                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Permissions fetched by role",
                                list
                        )
                );
    }

    // ============================================
    // UPDATE ROLE PERMISSION
    // ============================================

    @PutMapping("/{id}")
    public Mono<ApiResponse<RolePermissionResponse>>
    update(
            @PathVariable Long id,
            @RequestBody RolePermissionRequest rp
    ) {

        return rolePermissionService
                .update(id, rp)

                .map(r ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Role permission updated",
                                r
                        )
                );
    }

    // ============================================
    // REMOVE PERMISSION FROM ROLE
    // ============================================

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>>
    delete(@PathVariable Long id) {

        return rolePermissionService
                .delete(id)

                .then(
                        Mono.just(
                                new ApiResponse<>(
                                        "success",
                                        200,
                                        "Role permission removed",
                                        "Deleted"
                                )
                        )
                );
    }
}
//package com.fokatindia.user_service.controller;
//
//import com.fokatindia.user_service.dto.ApiResponse;
//import com.fokatindia.user_service.dto.RolePermissionRequest;
//import com.fokatindia.user_service.dto.RolePermissionResponse;
//import com.fokatindia.user_service.entity.RolePermission;
//import com.fokatindia.user_service.service.RolePermissionService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/restful/v1/api/role-permissions")
//public class RolePermissionController {
//
//    private final RolePermissionService rolePermissionService;
//
//    public RolePermissionController(RolePermissionService rolePermissionService) { this.rolePermissionService = rolePermissionService; }
//
//    @PostMapping
//    public Mono<ApiResponse<RolePermissionResponse>> assignPermission(@RequestBody RolePermissionRequest rp) {
//        return rolePermissionService.assignPermission(rp)
//                .map(r -> new ApiResponse<>("success", 200, "Permission assigned to role", r));
//    }
//}
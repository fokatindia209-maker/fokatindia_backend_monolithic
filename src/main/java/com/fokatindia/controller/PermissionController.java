package com.fokatindia.controller;


import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.PermissionRequest;
import com.fokatindia.dto.PermissionResponse;
import com.fokatindia.service.PermissionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(
            PermissionService permissionService
    ) {
        this.permissionService =
                permissionService;
    }

    // ============================================
    // CREATE PERMISSION
    // POST:
    // /restful/v1/api/permissions
    // ============================================

    @PostMapping
    public Mono<ApiResponse<PermissionResponse>>
    createPermission(
            @RequestBody PermissionRequest permission
    ) {

        return permissionService
                .createPermission(permission)

                .map(p ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Permission created",
                                p
                        )
                );
    }

    // ============================================
    // GET ALL PERMISSIONS
    // GET:
    // /restful/v1/api/permissions
    // ============================================

    @GetMapping
    public Mono<ApiResponse<List<PermissionResponse>>>
    getAllPermissions() {

        return permissionService
                .getAllPermissions()

                .collectList()

                .map(perms ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Permissions fetched",
                                perms
                        )
                );
    }

    // ============================================
    // GET PERMISSION BY ID
    // GET:
    // /restful/v1/api/permissions/{id}
    // ============================================

    @GetMapping("/{permissionId}")
    public Mono<ApiResponse<PermissionResponse>>
    getPermissionById(
            @PathVariable Long permissionId
    ) {

        return permissionService
                .getPermissionById(permissionId)

                .map(permission ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Permission fetched",
                                permission
                        )
                );
    }

    // ============================================
    // UPDATE PERMISSION
    // PUT:
    // /restful/v1/api/permissions/{id}
    // ============================================

    @PutMapping("/{permissionId}")
    public Mono<ApiResponse<PermissionResponse>>
    updatePermission(
            @PathVariable Long permissionId,
            @RequestBody PermissionRequest request
    ) {

        return permissionService
                .updatePermission(permissionId, request)

                .map(updated ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Permission updated",
                                updated
                        )
                );
    }

    // ============================================
    // DELETE PERMISSION
    // DELETE:
    // /restful/v1/api/permissions/{id}
    // ============================================

    @DeleteMapping("/{permissionId}")
    public Mono<ApiResponse<String>>
    deletePermission(
            @PathVariable Long permissionId
    ) {

        return permissionService
                .deletePermission(permissionId)

                .then(
                        Mono.just(
                                new ApiResponse<>(
                                        "success",
                                        200,
                                        "Permission deleted successfully",
                                        "Deleted"
                                )
                        )
                );
    }
}
//package com.fokatindia.user_service.controller;
//
//import com.fokatindia.user_service.dto.ApiResponse;
//import com.fokatindia.user_service.dto.PermissionRequest;
//import com.fokatindia.user_service.dto.PermissionResponse;
//import com.fokatindia.user_service.entity.Permission;
//import com.fokatindia.user_service.service.PermissionService;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/restful/v1/api/permissions")
//public class PermissionController {
//
//    private final PermissionService permissionService;
//
//    public PermissionController(PermissionService permissionService) { this.permissionService = permissionService; }
//
//    @PostMapping
//    public Mono<ApiResponse<PermissionResponse>> createPermission(@RequestBody PermissionRequest permission) {
//        return permissionService.createPermission(permission)
//                .map(p -> new ApiResponse<>("success", 200, "Permission created", p));
//    }
//
//    @GetMapping
//    public Mono<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
//        return permissionService.getAllPermissions()
//                .collectList()
//                .map(perms -> new ApiResponse<>("success", 200, "Permissions fetched", perms));
//    }
//}
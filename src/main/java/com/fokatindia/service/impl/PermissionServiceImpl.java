// ============================================
// PermissionServiceImpl.java
// ============================================

package com.fokatindia.service.impl;


import com.fokatindia.dto.PermissionRequest;
import com.fokatindia.dto.PermissionResponse;
import com.fokatindia.entity.Permission;
import com.fokatindia.repository.PermissionRepository;
import com.fokatindia.service.PermissionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PermissionServiceImpl
        implements PermissionService {

    private final PermissionRepository
            permissionRepository;

    public PermissionServiceImpl(
            PermissionRepository permissionRepository
    ) {
        this.permissionRepository =
                permissionRepository;
    }

    // ============================================
    // CREATE PERMISSION
    // ============================================

    @Override
    public Mono<PermissionResponse>
    createPermission(
            PermissionRequest request
    ) {

        return permissionRepository
                .findByName(
                        request.getName()
                )

                .flatMap(existing ->
                        Mono.error(
                                new RuntimeException(
                                        "Permission already exists"
                                )
                        )
                )

                .switchIfEmpty(

                        Mono.defer(() -> {

                            Permission permission =
                                    new Permission();

                            permission.setName(
                                    request.getName()
                            );

                            permission.setDescription(
                                    request.getDescription()
                            );

                            return permissionRepository
                                    .save(permission);
                        })
                )

                .cast(Permission.class)

                .map(this::mapToResponse);
    }

    // ============================================
    // GET ALL PERMISSIONS
    // ============================================

    @Override
    public Flux<PermissionResponse>
    getAllPermissions() {

        return permissionRepository
                .findAll()

                .map(this::mapToResponse);
    }

    // ============================================
    // GET PERMISSION BY ID
    // ============================================

    @Override
    public Mono<PermissionResponse>
    getPermissionById(
            Long permissionId
    ) {

        return permissionRepository
                .findById(permissionId)

                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException(
                                        "Permission not found"
                                )
                        )
                )

                .map(this::mapToResponse);
    }

    // ============================================
    // UPDATE PERMISSION
    // ============================================

    @Override
    public Mono<PermissionResponse>
    updatePermission(
            Long permissionId,
            PermissionRequest request
    ) {

        return permissionRepository
                .findById(permissionId)

                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException(
                                        "Permission not found"
                                )
                        )
                )

                .flatMap(existing -> {

                    if (request.getName() != null) {
                        existing.setName(request.getName());
                    }

                    if (request.getDescription() != null) {
                        existing.setDescription(request.getDescription());
                    }




                    return permissionRepository
                            .save(existing);
                })

                .map(this::mapToResponse);
    }

    // ============================================
    // DELETE PERMISSION
    // ============================================

    @Override
    public Mono<Void> deletePermission(
            Long permissionId
    ) {

        return permissionRepository
                .deleteById(permissionId);
    }

    // ============================================
    // ENTITY -> RESPONSE
    // ============================================

    private PermissionResponse mapToResponse(
            Permission permission
    ) {

        return new PermissionResponse(
                permission.getPermissionId(),
                permission.getName(),
                permission.getDescription()
        );
    }
}
//package com.fokatindia.user_service.service.impl;
//
//import com.fokatindia.user_service.dto.PermissionRequest;
//import com.fokatindia.user_service.dto.PermissionResponse;
//import com.fokatindia.user_service.entity.Permission;
//import com.fokatindia.user_service.repository.PermissionRepository;
//import com.fokatindia.user_service.service.PermissionService;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Service
//public class PermissionServiceImpl implements PermissionService {
//
//    private final PermissionRepository permissionRepository;
//
//    public PermissionServiceImpl(PermissionRepository permissionRepository) {
//        this.permissionRepository = permissionRepository;
//    }
//
//    @Override
//    public Mono<PermissionResponse> createPermission(PermissionRequest request) {
//        Permission permission = new Permission();
//        permission.setName(request.getName());
//        permission.setDescription(request.getDescription());
//
//        return permissionRepository.save(permission)
//                .map(saved -> new PermissionResponse(saved.getPermissionId(), saved.getName(), saved.getDescription()));
//    }
//
//    @Override
//    public Flux<PermissionResponse> getAllPermissions() {
//        return permissionRepository.findAll()
//                .map(p -> new PermissionResponse(p.getPermissionId(), p.getName(), p.getDescription()));
//    }
//}

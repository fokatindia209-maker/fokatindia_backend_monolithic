package com.fokatindia.service.impl;


import com.fokatindia.dto.RolePermissionRequest;
import com.fokatindia.dto.RolePermissionResponse;
import com.fokatindia.entity.RolePermission;
import com.fokatindia.repository.RolePermissionRepository;
import com.fokatindia.service.RolePermissionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RolePermissionServiceImpl
        implements RolePermissionService {

    private final RolePermissionRepository repository;

    public RolePermissionServiceImpl(
            RolePermissionRepository repository
    ) {
        this.repository = repository;
    }

    // ============================================
    // ASSIGN PERMISSION TO ROLE
    // ============================================

    @Override
    public Mono<RolePermissionResponse> assignPermission(
            RolePermissionRequest request
    ) {

        RolePermission rp = new RolePermission();

        rp.setRoleId(request.getRoleId());
        rp.setPermissionId(request.getPermissionId());

        return repository
                .save(rp)
                .map(this::mapToResponse);
    }

    // ============================================
    // GET ALL
    // ============================================

    @Override
    public Flux<RolePermissionResponse> getAllRolePermissions() {
        return repository
                .findAll()
                .map(this::mapToResponse);
    }

    // ============================================
    // GET BY ID
    // ============================================

    @Override
    public Mono<RolePermissionResponse> getById(Long id) {
        return repository
                .findById(id)
                .map(this::mapToResponse);
    }

    // ============================================
    // GET BY ROLE ID
    // ============================================

    @Override
    public Flux<RolePermissionResponse> getByRoleId(Long roleId) {
        return repository
                .findByRoleId(roleId)
                .map(this::mapToResponse);
    }

    // ============================================
    // UPDATE
    // ============================================

    @Override
    public Mono<RolePermissionResponse> update(
            Long id,
            RolePermissionRequest request
    ) {

        return repository
                .findById(id)
                .flatMap(existing -> {

                    existing.setRoleId(request.getRoleId());
                    existing.setPermissionId(request.getPermissionId());

                    return repository.save(existing);
                })
                .map(this::mapToResponse);
    }

    // ============================================
    // DELETE
    // ============================================

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    // ============================================
    // MAPPER
    // ============================================

    private RolePermissionResponse mapToResponse(
            RolePermission entity
    ) {

        return new RolePermissionResponse(
                entity.getId(),
                entity.getRoleId(),
                entity.getPermissionId()
        );
    }
}
//package com.fokatindia.user_service.service.impl;
//
//import com.fokatindia.user_service.dto.RolePermissionRequest;
//import com.fokatindia.user_service.dto.RolePermissionResponse;
//import com.fokatindia.user_service.entity.RolePermission;
//import com.fokatindia.user_service.repository.RolePermissionRepository;
//import com.fokatindia.user_service.service.RolePermissionService;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//@Service
//public class RolePermissionServiceImpl implements RolePermissionService {
//
//    private final RolePermissionRepository rolePermissionRepository;
//
//    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
//        this.rolePermissionRepository = rolePermissionRepository;
//    }
//
//    @Override
//    public Mono<RolePermissionResponse> assignPermission(RolePermissionRequest request) {
//        RolePermission rp = new RolePermission();
//        rp.setRoleId(request.getRoleId());
//        rp.setPermissionId(request.getPermissionId());
//
//        return rolePermissionRepository.save(rp)
//                .map(saved -> new RolePermissionResponse(saved.getId(), saved.getRoleId(), saved.getPermissionId()));
//    }
//}

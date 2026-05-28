// ============================================
// RoleServiceImpl.java
// ============================================

package com.fokatindia.service.impl;


import com.fokatindia.dto.RoleRequest;
import com.fokatindia.dto.RoleResponse;
import com.fokatindia.entity.Role;
import com.fokatindia.repository.RoleRepository;
import com.fokatindia.service.RoleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RoleServiceImpl
        implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(
            RoleRepository roleRepository
    ) {
        this.roleRepository =
                roleRepository;
    }

    // ============================================
    // CREATE ROLE
    // ============================================

    @Override
    public Mono<RoleResponse> createRole(
            RoleRequest request
    ) {

        return roleRepository
                .findByName(
                        request.getName()
                )

                .flatMap(existing ->
                        Mono.error(
                                new RuntimeException(
                                        "Role already exists"
                                )
                        )
                )

                .switchIfEmpty(

                        Mono.defer(() -> {

                            Role role =
                                    new Role();

                            role.setName(
                                    request.getName()
                            );

                            role.setDescription(
                                    request.getDescription()
                            );

                            return roleRepository
                                    .save(role);
                        })
                )

                .cast(Role.class)

                .map(this::mapToResponse);
    }

    // ============================================
    // GET ALL ROLES
    // ============================================

    @Override
    public Flux<RoleResponse> getAllRoles() {

        return roleRepository
                .findAll()
                .map(this::mapToResponse);
    }

    // ============================================
    // GET ROLE BY ID
    // ============================================

    @Override
    public Mono<RoleResponse> getRoleById(
            Long roleId
    ) {

        return roleRepository
                .findById(roleId)

                .map(this::mapToResponse);
    }

    // ============================================
    // UPDATE ROLE
    // ============================================

    @Override
    public Mono<RoleResponse> updateRole(
            Long roleId,
            RoleRequest request
    ) {

        return roleRepository
                .findById(roleId)

                .switchIfEmpty(
                        Mono.error(
                                new RuntimeException(
                                        "Role not found"
                                )
                        )
                )

                .flatMap(existing -> {

                    existing.setName(
                            request.getName()
                    );

                    existing.setDescription(
                            request.getDescription()
                    );

                    return roleRepository
                            .save(existing);
                })

                .map(this::mapToResponse);
    }

    // ============================================
    // DELETE ROLE
    // ============================================

    @Override
    public Mono<Void> deleteRole(
            Long roleId
    ) {

        return roleRepository
                .deleteById(roleId);
    }

    // ============================================
    // ENTITY -> RESPONSE
    // ============================================

    private RoleResponse mapToResponse(
            Role role
    ) {

        return new RoleResponse(
                role.getRoleId(),
                role.getName(),
                role.getDescription()
        );
    }
}
//package com.fokatindia.user_service.service.impl;
//
//import com.fokatindia.user_service.dto.RoleRequest;
//import com.fokatindia.user_service.dto.RoleResponse;
//import com.fokatindia.user_service.entity.Role;
//import com.fokatindia.user_service.repository.RoleRepository;
//import com.fokatindia.user_service.service.RoleService;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Service
//public class RoleServiceImpl implements RoleService {
//
//    private final RoleRepository roleRepository;
//
//    public RoleServiceImpl(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public Mono<RoleResponse> createRole(RoleRequest request) {
//        Role role = new Role();
//        role.setName(request.getName());
//        role.setDescription(request.getDescription());
//
//        return roleRepository.save(role)
//                .map(saved -> new RoleResponse(saved.getRoleId(), saved.getName(), saved.getDescription()));
//    }
//
//    @Override
//    public Flux<RoleResponse> getAllRoles() {
//        return roleRepository.findAll()
//                .map(r -> new RoleResponse(r.getRoleId(), r.getName(), r.getDescription()));
//    }
//}
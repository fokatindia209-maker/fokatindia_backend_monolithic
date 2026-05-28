// ============================================
// UserRoleServiceImpl.java
// ============================================

package com.fokatindia.service.impl;


import com.fokatindia.dto.UserRoleRequest;
import com.fokatindia.dto.UserRoleResponse;
import com.fokatindia.entity.UserRole;
import com.fokatindia.repository.UserRoleRepository;
import com.fokatindia.service.UserRoleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserRoleServiceImpl
        implements UserRoleService {

    private final UserRoleRepository
            userRoleRepository;

    public UserRoleServiceImpl(
            UserRoleRepository userRoleRepository
    ) {
        this.userRoleRepository =
                userRoleRepository;
    }

    // ============================================
    // ASSIGN ROLE
    // ============================================

    @Override
    public Mono<UserRoleResponse> assignRole(
            UserRoleRequest request
    ) {

        UserRole userRole = new UserRole();

        userRole.setUserId(
                request.getUserId()
        );

        userRole.setRoleId(
                request.getRoleId()
        );

        return userRoleRepository
                .save(userRole)

                .map(this::mapToResponse);
    }

    // ============================================
    // GET ALL USER ROLES
    // ============================================

    @Override
    public Flux<UserRoleResponse>
    getAllUserRoles() {

        return userRoleRepository
                .findAll()

                .map(this::mapToResponse);
    }

    // ============================================
    // GET USER ROLE BY ID
    // ============================================

    @Override
    public Mono<UserRoleResponse>
    getUserRoleById(Long id) {

        return userRoleRepository
                .findById(id)

                .map(this::mapToResponse);
    }

    // ============================================
    // GET ROLES BY USER ID
    // ============================================

    @Override
    public Flux<UserRoleResponse>
    getRolesByUserId(Long userId) {

        return userRoleRepository
                .findByUserId(userId)

                .map(this::mapToResponse);
    }

    // ============================================
    // UPDATE USER ROLE
    // ============================================

    @Override
    public Mono<UserRoleResponse>
    updateUserRole(
            Long id,
            UserRoleRequest request
    ) {

        return userRoleRepository
                .findById(id)

                .flatMap(existing -> {

                    existing.setUserId(
                            request.getUserId()
                    );

                    existing.setRoleId(
                            request.getRoleId()
                    );

                    return userRoleRepository
                            .save(existing);
                })

                .map(this::mapToResponse);
    }

    // ============================================
    // REMOVE ROLE
    // ============================================

    @Override
    public Mono<Void> removeRole(Long id) {

        return userRoleRepository
                .deleteById(id);
    }

    // ============================================
    // ENTITY -> RESPONSE
    // ============================================

    private UserRoleResponse mapToResponse(
            UserRole userRole
    ) {

        return new UserRoleResponse(
                userRole.getId(),
                userRole.getUserId(),
                userRole.getRoleId()
        );
    }
}
//package com.fokatindia.user_service.service.impl;
//
//import com.fokatindia.user_service.dto.UserRoleRequest;
//import com.fokatindia.user_service.dto.UserRoleResponse;
//import com.fokatindia.user_service.entity.UserRole;
//import com.fokatindia.user_service.repository.UserRoleRepository;
//import com.fokatindia.user_service.service.UserRoleService;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//@Service
//public class UserRoleServiceImpl implements UserRoleService {
//
//    private final UserRoleRepository userRoleRepository;
//
//    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
//        this.userRoleRepository = userRoleRepository;
//    }
//
//    @Override
//    public Mono<UserRoleResponse> assignRole(UserRoleRequest request) {
//        UserRole ur = new UserRole();
//        ur.setUserId(request.getUserId());
//        ur.setRoleId(request.getRoleId());
//
//        return userRoleRepository.save(ur)
//                .map(saved -> new UserRoleResponse(saved.getId(), saved.getUserId(), saved.getRoleId()));
//    }
//}

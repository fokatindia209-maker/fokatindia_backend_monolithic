package com.fokatindia.config;

import com.fokatindia.entity.Permission;
import com.fokatindia.entity.RolePermission;
import com.fokatindia.repository.PermissionRepository;
import com.fokatindia.repository.RolePermissionRepository;
import com.fokatindia.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    // Permissions that VENDOR and SUB_VENDOR must always have
    private static final List<String[]> REQUIRED_PERMISSIONS = List.of(
            new String[]{"PROFILE_VIEW",   "View own vendor/subvendor profile"},
            new String[]{"PROFILE_UPDATE", "Update own vendor/subvendor profile"}
    );

    private static final List<String> PARTNER_ROLES = List.of("VENDOR", "SUB_VENDOR");

    @EventListener(ApplicationReadyEvent.class)
    public void seedProfilePermissions() {
        Flux.fromIterable(REQUIRED_PERMISSIONS)
                .flatMap(entry -> ensurePermission(entry[0], entry[1]))
                .collectList()
                .flatMap(permissions ->
                        Flux.fromIterable(PARTNER_ROLES)
                                .flatMap(roleName ->
                                        roleRepository.findByName(roleName)
                                                .flatMap(role ->
                                                        Flux.fromIterable(permissions)
                                                                .flatMap(permission ->
                                                                        ensureRolePermission(role.getRoleId(), permission.getPermissionId())
                                                                )
                                                                .then()
                                                )
                                                .doOnError(e -> log.warn("Role '{}' not found, skipping", roleName))
                                                .onErrorResume(e -> Mono.empty())
                                )
                                .then()
                )
                .doOnSuccess(v -> log.info("Profile permissions seeded for VENDOR and SUB_VENDOR roles"))
                .doOnError(e -> log.error("Failed to seed profile permissions", e))
                .subscribe();
    }

    private Mono<Permission> ensurePermission(String name, String description) {
        return permissionRepository.findByName(name)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            Permission p = new Permission();
                            p.setName(name);
                            p.setDescription(description);
                            return permissionRepository.save(p)
                                    .doOnSuccess(saved -> log.info("Created permission: {}", name));
                        })
                );
    }

    private Mono<Void> ensureRolePermission(Long roleId, Long permissionId) {
        return rolePermissionRepository.findByRoleId(roleId)
                .filter(rp -> rp.getPermissionId().equals(permissionId))
                .hasElements()
                .flatMap(exists -> {
                    if (exists) return Mono.empty();
                    RolePermission rp = new RolePermission();
                    rp.setRoleId(roleId);
                    rp.setPermissionId(permissionId);
                    return rolePermissionRepository.save(rp)
                            .doOnSuccess(saved -> log.info("Assigned permission {} to role {}", permissionId, roleId))
                            .then();
                });
    }
}

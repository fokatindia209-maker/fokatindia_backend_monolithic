package com.fokatindia.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("role_permissions")
public class RolePermission {
    @Id
    private Long id;
    private Long roleId;
    private Long permissionId;
}
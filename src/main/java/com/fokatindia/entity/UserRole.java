package com.fokatindia.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("user_roles")
public class UserRole {
    @Id
    private Long id;
    private Long userId;
    private Long roleId;
}
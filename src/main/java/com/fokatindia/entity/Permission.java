package com.fokatindia.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("permissions")
public class Permission {
    @Id
    private Long permissionId;
    private String name;
    private String description;
}
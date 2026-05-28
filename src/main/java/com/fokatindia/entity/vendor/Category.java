package com.fokatindia.entity.vendor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("categories")
public class Category {

    @Id
    private Long id;

    private String name;

    private String description;

    private Boolean active;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Display order in app
    private Integer displayOrder;

    // SEO / unique slug
    private String slug;
}

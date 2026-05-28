package com.fokatindia.dto.vendor;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private Integer displayOrder;

    private Boolean active;

    private String slug;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
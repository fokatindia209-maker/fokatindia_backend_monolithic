// =========================================
// CATEGORY DTOs
// =========================================

package com.fokatindia.dto.vendor;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;

    private String description;

    private String imageUrl;

    private Integer displayOrder;

    private String slug;
}
// =========================================
// CATEGORY DTOs
// =========================================

package com.fokatindia.dto.vendor;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

@Data
public class CategoryRequest {

    private String name;

    private String description;

    private FilePart imageUrl;

    private Integer displayOrder;

    private String slug;

    private Boolean active;
}
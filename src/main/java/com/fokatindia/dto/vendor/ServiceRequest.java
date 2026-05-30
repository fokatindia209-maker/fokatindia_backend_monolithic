package com.fokatindia.dto.vendor;

// ================================
// DTOs
// ================================


import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

@Data
public class ServiceRequest {

    private Long categoryId;
    private String name;
    private String description;

    private Double price;
    private Double discountedPrice;
    private Integer durationMinutes;

    private String serviceCode;
    private Boolean featured;
    private Boolean active;

    private String serviceType;
    private String slug;

    private FilePart file;
}
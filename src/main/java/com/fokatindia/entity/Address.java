package com.fokatindia.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("addresses")
public class Address {

    @Id
    private Long addressId;

    private Long userId;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    private String country;

    private Double latitude;

    private Double longitude;

    private Boolean isDefault;

    private LocalDateTime createdAt;
}
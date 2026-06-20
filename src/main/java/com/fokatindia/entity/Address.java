package com.fokatindia.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("addresses")
public class Address {

    @Id
    @Column("address_id")
    private Long addressId;

    @Column("user_id")
    private Long userId;

    @Column("address_line1")
    private String addressLine1;

    @Column("address_line2")
    private String addressLine2;

    @Column("city")
    private String city;

    @Column("state")
    private String state;

    @Column("pincode")
    private String pincode;

    @Column("country")
    private String country;

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("is_default")
    private Boolean isDefault;

    @Column("created_at")
    private LocalDateTime createdAt;
}
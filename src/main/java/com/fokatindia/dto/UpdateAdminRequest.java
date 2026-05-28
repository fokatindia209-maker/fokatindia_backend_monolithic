package com.fokatindia.dto;

import lombok.Data;

@Data
public class UpdateAdminRequest {
    private String name;
    private String phone;
    private String role;
    private String status;
}
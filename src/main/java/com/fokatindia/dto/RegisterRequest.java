package com.fokatindia.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String status;
    // Only for Vendor
    private String invitationCode;

}
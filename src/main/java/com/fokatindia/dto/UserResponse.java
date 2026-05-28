package com.fokatindia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String token;
    private String status;
    private String documentStatus;
    private String invitationCode;
}
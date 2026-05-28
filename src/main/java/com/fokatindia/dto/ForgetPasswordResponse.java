package com.fokatindia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgetPasswordResponse {
    private String email;
    private String message;
}
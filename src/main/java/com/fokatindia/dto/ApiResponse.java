package com.fokatindia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;       // "success" or "error"
    private int statusCode;      // e.g. 200, 400, 500
    private String message;      // human-readable message
    private T data;              // object or list
    private String token;


    // Convenience constructor for responses without token
    public ApiResponse(String status, int statusCode, String message, T data) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.token = null;
    }

    // Convenience constructor for responses with token only
    public ApiResponse(String status, int statusCode, String message, String token) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
        this.token = token;
    }
}
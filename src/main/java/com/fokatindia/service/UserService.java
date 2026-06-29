package com.fokatindia.service;

import com.fokatindia.dto.ForgetPasswordResponse;
import com.fokatindia.dto.LoginRequest;
import com.fokatindia.dto.PhoneLoginRequest;
import com.fokatindia.dto.RegisterRequest;
import com.fokatindia.dto.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {

    Mono<UserResponse> registerUser(RegisterRequest request);

    Mono<UserResponse> registerVendor(RegisterRequest request);

    Mono<UserResponse> registerSubVendor(RegisterRequest request);
    Mono<UserResponse> login(LoginRequest request);

    Mono<UserResponse> phoneLogin(PhoneLoginRequest request);

    Mono<ForgetPasswordResponse> forgotPassword(String email);
    Mono<UserResponse> getProfile(Long userId);
    Mono<UserResponse> updateProfile(Long id, RegisterRequest request);
    Mono<Void> deleteUser(Long id);


    Mono<UserResponse> deactivateUser(Long id);

    Mono<UserResponse> activateUser(Long id);
    Mono<List<String>> getPermissions(Long userId);
    Flux<UserResponse> getAllUsers();






}

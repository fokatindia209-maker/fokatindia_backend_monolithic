package com.fokatindia.controller;

import com.fokatindia.dto.*;
import com.fokatindia.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public Mono<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request)
                .map(user -> new ApiResponse<>("success", 200, "User registered successfully", user));
    }

    @PostMapping("/admin/vendors")
    public Mono<ApiResponse<UserResponse>> registerVendor(@RequestBody RegisterRequest request) {
        return userService.registerVendor(request)
                .map(user -> new ApiResponse<>("success", 200, "Vendor created successfully", user));
    }

    @PostMapping("/admin/subvendors")
    public Mono<ApiResponse<UserResponse>> registerSubVendor(@RequestBody RegisterRequest request) {
        return userService.registerSubVendor(request)
                .map(user -> new ApiResponse<>("success", 200, "SubVendor created successfully", user));
    }

    @PostMapping("/login")
    public Mono<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        return userService.login(request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Login successful",
                        res
                ));
    }

    @PostMapping("/forgot-password")
    public Mono<ApiResponse<ForgetPasswordResponse>> forgotPassword(@RequestBody ForgetPasswordRequest request) {
        return userService.forgotPassword(request.getEmail())
                .map(msg -> new ApiResponse<>("success", 200, "Reset link sent", msg));
    }

    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @GetMapping("/{userId}")
    public Mono<ApiResponse<UserResponse>> getProfile(@PathVariable Long userId) {
        return userService.getProfile(userId)
                .map(user -> new ApiResponse<>("success", 200, "Profile fetched", user));
    }


    @PreAuthorize("hasAuthority('VIEW_ALL_USER')")
    @GetMapping
    public Mono<ApiResponse<List<UserResponse>>> getAllUsers() {
        return userService.getAllUsers()
                .collectList()
                .map(user -> new ApiResponse<>("success", 200, "Profile fetched", user));
    }




    @PreAuthorize("hasAuthority('PROFILE_UPDATE')")
    @PutMapping("/{userId}")
    public Mono<ApiResponse<UserResponse>> updateProfile(@PathVariable Long userId, @RequestBody RegisterRequest request) {
        return userService.updateProfile(userId, request)
                .map(user -> new ApiResponse<>("success", 200, "Profile updated", user));
    }
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    @DeleteMapping("/{userId}")
    public Mono<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId)
                .thenReturn(new ApiResponse<>("success", 200, "User deleted", null));
    }

    @PutMapping("/{userId}/deactivate")
    public Mono<ApiResponse<UserResponse>> deactivate(@PathVariable Long userId) {
        return userService.deactivateUser(userId)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "User deactivated",
                        res
                ));
    }

    @PutMapping("/{userId}/activate")
    public Mono<ApiResponse<UserResponse>> activate(@PathVariable Long userId) {
        return userService.activateUser(userId)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "User activated",
                        res
                ));
    }
}

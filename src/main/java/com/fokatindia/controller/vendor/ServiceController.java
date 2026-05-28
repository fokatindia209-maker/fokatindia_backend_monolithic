package com.fokatindia.controller.vendor;



import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.vendor.ServiceRequest;
import com.fokatindia.dto.vendor.ServiceResponse;
import com.fokatindia.service.vendor.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService service;

    // =====================================================
    // CREATE SERVICE
    // =====================================================

    @PreAuthorize("hasAuthority('SERVICE_ADD')")
    @PostMapping
    public Mono<ApiResponse<ServiceResponse>> create(
            @RequestBody ServiceRequest request
    ) {

        return service.create(request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Service created successfully",
                                res
                        )
                );
    }

    // =====================================================
    // GET SERVICE BY ID
    // =====================================================

    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
    @GetMapping("/{id}")
    public Mono<ApiResponse<ServiceResponse>> getById(
            @PathVariable Long id
    ) {

        return service.getById(id)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Service fetched successfully",
                                res
                        )
                );
    }

    // =====================================================
    // GET ALL SERVICES
    // =====================================================

    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
    @GetMapping
    public Mono<ApiResponse<List<ServiceResponse>>> getAll() {

        return service.getAll()
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Service list fetched successfully",
                                list
                        )
                );
    }

    // =====================================================
    // GET SERVICES BY CATEGORY
    // =====================================================

    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
    @GetMapping("/category/{categoryId}")
    public Mono<ApiResponse<List<ServiceResponse>>> getByCategory(
            @PathVariable Long categoryId
    ) {

        return service.getByCategory(categoryId)
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Category services fetched successfully",
                                list
                        )
                );
    }

    // =====================================================
    // UPDATE SERVICE
    // =====================================================

    @PreAuthorize("hasAuthority('SERVICE_UPDATE')")
    @PutMapping("/{id}")
    public Mono<ApiResponse<ServiceResponse>> update(
            @PathVariable Long id,
            @RequestBody ServiceRequest request
    ) {

        return service.update(id, request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Service updated successfully",
                                res
                        )
                );
    }

    // =====================================================
    // DELETE SERVICE
    // =====================================================

    @PreAuthorize("hasAuthority('SERVICE_DELETE')")
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> delete(
            @PathVariable Long id
    ) {

        return service.delete(id)
                .thenReturn(
                        new ApiResponse<>(
                                "success",
                                200,
                                "Service deleted successfully",
                                "Deleted"
                        )
                );
    }
}
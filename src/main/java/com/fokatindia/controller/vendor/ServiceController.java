package com.fokatindia.controller.vendor;



import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.vendor.CategoryResponse;
import com.fokatindia.dto.vendor.ServiceRequest;
import com.fokatindia.dto.vendor.ServiceResponse;
import com.fokatindia.service.vendor.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ApiResponse<ServiceResponse>> create(

            @RequestPart("categoryId") String categoryId,
            @RequestPart("name") String name,
            @RequestPart("description") String description,
            @RequestPart("price") String price,
            @RequestPart("discountedPrice") String discountedPrice,
            @RequestPart("durationMinutes") String durationMinutes,
            @RequestPart(value = "serviceCode", required = false) String serviceCode,
            @RequestPart(value = "featured", required = false) String featured,
            @RequestPart(value = "active", required = false) String active,
            @RequestPart(value = "serviceType", required = false) String serviceType,
            @RequestPart(value = "slug", required = false) String slug,
            @RequestPart(value = "file", required = false) FilePart file
    ) {

        ServiceRequest request = new ServiceRequest();

        request.setCategoryId(Long.valueOf(categoryId));
        request.setName(name);
        request.setDescription(description);
        request.setPrice(Double.valueOf(price));
        request.setDiscountedPrice(Double.valueOf(discountedPrice));
        request.setDurationMinutes(Integer.valueOf(durationMinutes));

        request.setServiceCode(serviceCode);
        request.setFeatured(Boolean.valueOf(featured));
        request.setActive(Boolean.valueOf(active));
        request.setServiceType(serviceType);
        request.setSlug(slug);
        request.setFile(file);
        return service.create(request)
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Service created successfully",
                        res
                ));
    }
//    @PreAuthorize("hasAuthority('SERVICE_ADD')")
//    @PostMapping
//    public Mono<ApiResponse<ServiceResponse>> create(
//            @RequestBody ServiceRequest request
//    ) {
//
//        return service.create(request)
//                .map(res ->
//                        new ApiResponse<>(
//                                "success",
//                                200,
//                                "Service created successfully",
//                                res
//                        )
//                );
//    }

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

    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
    @GetMapping("/vendors/{vendorId}")
    public Mono<ApiResponse<List<ServiceResponse>>> getByVendorId(@PathVariable Long vendorId) {

        return service.getByVendorId(vendorId)
                .collectList()
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Services fetched successfully",
                        res
                ));
    }


    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
    @GetMapping("/subVendors/{subVendorId}")
    public Mono<ApiResponse<List<ServiceResponse>>> getBySubVendorId(@PathVariable Long subVendorId) {

        return service.getBySubVendorId(subVendorId)
                .collectList()
                .map(res -> new ApiResponse<>(
                        "success",
                        200,
                        "Services fetched successfully",
                        res
                ));
    }
    // =====================================================
    // GET ALL SERVICES
    // =====================================================

//    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
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

//    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
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
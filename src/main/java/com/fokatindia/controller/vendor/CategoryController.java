package com.fokatindia.controller.vendor;


import com.fokatindia.dto.ApiResponse;
import com.fokatindia.dto.vendor.CategoryRequest;
import com.fokatindia.dto.vendor.CategoryResponse;
import com.fokatindia.service.vendor.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restful/v1/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    // =====================================================
    // CREATE CATEGORY
    // =====================================================

    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Mono<ApiResponse<CategoryResponse>> create(
            @RequestPart("name") String name,

            @RequestPart("description") String description,

            @RequestPart("displayOrder") String displayOrder,

            @RequestPart("slug") String slug,

            @RequestPart("active") String active,

            @RequestPart("file") FilePart file
    ) {
        CategoryRequest request = new CategoryRequest();

        request.setName(name);
        request.setDescription(description);
        request.setDisplayOrder(Integer.valueOf(displayOrder));
        request.setSlug(slug);
        request.setActive(Boolean.valueOf(active));
        request.setImageUrl(file);
        return service.create(request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Category created successfully",
                                res
                        )
                );
    }

    // =====================================================
    // GET CATEGORY BY ID
    // =====================================================

    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    @GetMapping("/{id}")
    public Mono<ApiResponse<CategoryResponse>> getById(
            @PathVariable Long id
    ) {

        return service.getById(id)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Category fetched successfully",
                                res
                        )
                );
    }

    // =====================================================
    // GET ALL CATEGORIES
    // =====================================================

//    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    @GetMapping
    public Mono<ApiResponse<List<CategoryResponse>>> getAll() {

        return service.getAll()
                .collectList()
                .map(list ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Category list fetched successfully",
                                list
                        )
                );
    }

    // =====================================================
    // UPDATE CATEGORY
    // =====================================================

    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    @PostMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Mono<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @RequestPart(value = "name", required = false) String name,

            @RequestPart(value = "description", required = false) String description,

            @RequestPart(value = "displayOrder", required = false) String displayOrder,

            @RequestPart(value = "slug", required = false) String slug,

            @RequestPart(value = "active", required = false) String active,

            @RequestPart(value = "file", required = false) FilePart file
    ) {
        CategoryRequest request = new CategoryRequest();

        request.setName(name);
        request.setDescription(description);
        request.setSlug(slug);

        if (displayOrder != null)
            request.setDisplayOrder(Integer.valueOf(displayOrder));

        if (active != null)
            request.setActive(Boolean.valueOf(active));

        request.setImageUrl(file);
        return service.update(id, request)
                .map(res ->
                        new ApiResponse<>(
                                "success",
                                200,
                                "Category updated successfully",
                                res
                        )
                );
    }

    // =====================================================
    // DELETE CATEGORY
    // =====================================================

    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> delete(
            @PathVariable Long id
    ) {

        return service.delete(id)
                .thenReturn(
                        new ApiResponse<>(
                                "success",
                                200,
                                "Category deleted successfully",
                                "Deleted"
                        )
                );
    }


}
package org.zafu.bookservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zafu.bookservice.dto.ApiResponse;
import org.zafu.bookservice.dto.request.CategoryRequest;
import org.zafu.bookservice.dto.response.CategoryResponse;
import org.zafu.bookservice.dto.response.CategoryResponse;
import org.zafu.bookservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get all categories successfully")
                .result(service.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable Integer id) {
        return ApiResponse.<CategoryResponse>builder()
                .message("Get category with id: " + id)
                .result(service.getById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .message("Create category successfully")
                .result(service.createCategory(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .message("Update category successfully")
                .result(service.updateCategory(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCategory(@PathVariable Integer id) {
        service.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .message("Create category successfully")
                .build();
    }

}

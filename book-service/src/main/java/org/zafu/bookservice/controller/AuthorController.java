package org.zafu.bookservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zafu.bookservice.dto.ApiResponse;
import org.zafu.bookservice.dto.request.AuthorRequest;
import org.zafu.bookservice.dto.response.AuthorResponse;
import org.zafu.bookservice.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @GetMapping
    public ApiResponse<List<AuthorResponse>> getAllAuthors() {
        return ApiResponse.<List<AuthorResponse>>builder()
                .message("Get all authors")
                .result(service.getAll())
                .build();
    }

    @GetMapping("/category/{category}")
    public ApiResponse<List<AuthorResponse>> getByCategory(
            @PathVariable String category
    ) {
        return ApiResponse.<List<AuthorResponse>>builder()
                .message("Get all authors by category: " + category)
                .result(service.getByCategory(category))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AuthorResponse> getById(@PathVariable Integer id) {
        return ApiResponse.<AuthorResponse>builder()
                .message("Get author with id: " + id)
                .result(service.getById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AuthorResponse> createAuthor(
            @Valid @RequestBody AuthorRequest request) {
        return ApiResponse.<AuthorResponse>builder()
                .message("Create author successfully")
                .result(service.createAuthor(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AuthorResponse> updateAuthor(@PathVariable Integer id, @Valid @RequestBody AuthorRequest request) {
        return ApiResponse.<AuthorResponse>builder()
                .message("Update author successfully")
                .result(service.updateAuthor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteAuthor(@PathVariable Integer id) {
        service.deleteAuthor(id);
        return ApiResponse.<Void>builder()
                .message("Create author successfully")
                .build();
    }
}

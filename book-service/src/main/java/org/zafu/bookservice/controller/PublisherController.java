package org.zafu.bookservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zafu.bookservice.dto.ApiResponse;
import org.zafu.bookservice.dto.request.PublisherRequest;
import org.zafu.bookservice.dto.response.PublisherResponse;
import org.zafu.bookservice.dto.response.PublisherResponse;
import org.zafu.bookservice.service.PublisherService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService service;

    @GetMapping
    public ApiResponse<List<PublisherResponse>> getAllPublishers() {
        return ApiResponse.<List<PublisherResponse>>builder()
                .message("Get all publishers successfully")
                .result(service.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PublisherResponse> getById(@PathVariable Integer id) {
        return ApiResponse.<PublisherResponse>builder()
                .message("Get publisher with id: " + id)
                .result(service.getById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PublisherResponse> createPublisher(
            @Valid @RequestBody PublisherRequest request) {
        return ApiResponse.<PublisherResponse>builder()
                .message("Create author successfully")
                .result(service.createPublisher(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PublisherResponse> updatePublisher(@PathVariable Integer id, @Valid @RequestBody PublisherRequest request) {
        return ApiResponse.<PublisherResponse>builder()
                .message("Update author successfully")
                .result(service.updatePublisher(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deletePublisher(@PathVariable Integer id) {
        service.deletePublisher(id);
        return ApiResponse.<Void>builder()
                .message("Create author successfully")
                .build();
    }

}

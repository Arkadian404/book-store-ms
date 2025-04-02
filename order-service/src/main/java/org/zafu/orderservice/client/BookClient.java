package org.zafu.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.zafu.orderservice.dto.ApiResponse;
import org.zafu.orderservice.dto.request.UpdateStockRequest;
import org.zafu.orderservice.dto.response.BookResponse;

import java.util.Optional;

@FeignClient(
        name = "book-service",
        url = "${app.client.book-url}"
)
public interface BookClient {
    @GetMapping("/books/{id}")
    Optional<ApiResponse<BookResponse>> getBookById(@PathVariable Integer id);

    @PutMapping("/books/{id}/stock")
    Optional<ApiResponse<BookResponse>> updateStock(@PathVariable Integer id, UpdateStockRequest request);
}

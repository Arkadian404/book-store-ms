package org.zafu.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.zafu.paymentservice.dto.ApiResponse;
import org.zafu.paymentservice.dto.request.UpdateStockRequest;
import org.zafu.paymentservice.dto.response.BookResponse;

import java.util.Optional;

@FeignClient(
        name = "book-service",
        url = "${app.client.book-url}"
)
public interface BookClient {
    @PutMapping("/books/{id}/stock")
    Optional<ApiResponse<BookResponse>> updateStock(@PathVariable Integer id, UpdateStockRequest request);
}

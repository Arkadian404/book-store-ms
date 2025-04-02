package org.zafu.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.zafu.cartservice.dto.ApiResponse;
import org.zafu.cartservice.dto.response.BookResponse;

import java.util.Optional;

@FeignClient(
        name = "book-service",
        url = "${app.client.book-url}"
)
public interface BookClient {
    @GetMapping("/books/{id}")
    Optional<ApiResponse<BookResponse>> getBookById(@PathVariable Integer id);
}

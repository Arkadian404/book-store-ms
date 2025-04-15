package org.zafu.cartservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zafu.cartservice.dto.ApiResponse;
import org.zafu.cartservice.dto.request.AddCartItemRequest;
import org.zafu.cartservice.dto.request.UpdateCartItemRequest;
import org.zafu.cartservice.dto.response.CartItemResponse;
import org.zafu.cartservice.dto.response.CartResponse;
import org.zafu.cartservice.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;


    @GetMapping("/user/{userId}")
    public ApiResponse<CartResponse> getCart(@PathVariable Integer userId){
        return ApiResponse.<CartResponse>builder()
                .message("Get cart successfully")
                .result(cartService.getCart(userId))
                .build();
    }

    @GetMapping("/user/{userId}/items")
    public ApiResponse<List<CartItemResponse>> getCartItems(@PathVariable Integer userId){
        return ApiResponse.<List<CartItemResponse>>builder()
                .message("Get cart items successfully")
                .result(cartService.getCartItems(userId))
                .build();
    }

    @PostMapping("/user/{userId}/items")
    public ApiResponse<CartItemResponse> addItemToCart(
        @PathVariable Integer userId,
        @RequestBody @Valid AddCartItemRequest request
    ){
        return ApiResponse.<CartItemResponse>builder()
                .message("Add item to cart successfully")
                .result(cartService.addItemToCart(userId, request))
                .build();
    }

    @PutMapping("/user/{userId}/items/{bookId}")
    public ApiResponse<CartItemResponse> updateCartItemQuantity(
        @PathVariable Integer userId,
        @PathVariable Integer bookId,
        @RequestBody @Valid UpdateCartItemRequest request
    ){
        return ApiResponse.<CartItemResponse>builder()
                .message("Update item quantity successfully")
                .result(cartService.updateCartItemQuantity(userId, bookId, request))
                .build();
    }

    @DeleteMapping("/user/{userId}/items/{bookId}")
    public ApiResponse<CartItemResponse> removeItemFromCart(
        @PathVariable Integer userId,
        @PathVariable Integer bookId
    ){
        cartService.removeItemFromCart(userId, bookId);
        return ApiResponse.<CartItemResponse>builder()
                .message("Remove item from cart successfully")
                .build();
    }

    @DeleteMapping("/user/{userId}")
    public ApiResponse<Void> clearCart(@PathVariable Integer userId){
        cartService.clearCart(userId);
        return ApiResponse.<Void>builder()
                .message("Clear cart successfully")
                .build();
    }
}

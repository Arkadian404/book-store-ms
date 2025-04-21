package org.zafu.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zafu.orderservice.dto.ApiResponse;
import org.zafu.orderservice.dto.PageResponse;
import org.zafu.orderservice.dto.request.CreateOrderRequest;
import org.zafu.orderservice.dto.request.UpdateOrderStatusRequest;
import org.zafu.orderservice.dto.response.OrderResponse;
import org.zafu.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<List<OrderResponse>> getAll(){
        return ApiResponse.<List<OrderResponse>>builder()
                .message("Orders found")
                .result(orderService.getAll())
                .build();
    }


    @GetMapping("/page")
    public ApiResponse<PageResponse<OrderResponse>> getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .message("Orders found")
                .result(orderService.getAllOrdersPaging(page, size))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResponse<OrderResponse>> getOrdersByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .message("Orders found")
                .result(orderService.getAllUserOrdersPaging(userId, page, size))
                .build();
    }

    @PostMapping("/user/{userId}")
    public ApiResponse<OrderResponse> createOrder(
            @PathVariable Integer userId,
            @RequestBody CreateOrderRequest createOrderRequest
    ) {
        return ApiResponse.<OrderResponse>builder()
                .message("Order created")
                .result(orderService.createOrder(userId, createOrderRequest))
                .build();
    }

    @GetMapping("/code/{orderCode}")
    public ApiResponse<OrderResponse> getOrderByCode(
            @PathVariable String orderCode
    ){
        return ApiResponse.<OrderResponse>builder()
                .message("Order found")
                .result(orderService.getOrderByOrderCode(orderCode))
                .build();
    }

    @PutMapping("/code/{orderCode}")
    public ApiResponse<Void> updateOrderStatus(
            @PathVariable String orderCode,
            @RequestBody UpdateOrderStatusRequest request
            ){
        orderService.updateOrderStatus(orderCode, request);
        return ApiResponse.<Void>builder()
                .message("Order status updated")
                .build();
    }
}

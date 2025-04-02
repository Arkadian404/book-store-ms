package org.zafu.orderservice.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.zafu.orderservice.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Integer id;
    private Integer userId;
    private String orderCode;
    private double totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String notes;
    private LocalDateTime createdDate;
    private List<OrderItemResponse> items;
    private String paymentUrl;
}

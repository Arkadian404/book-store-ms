package org.zafu.paymentservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderConfirmation {
    private String orderCode;
    private Integer userId;
    private double totalAmount;
    private String status;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdDate;
    private List<OrderItemConfirmation> items;

}

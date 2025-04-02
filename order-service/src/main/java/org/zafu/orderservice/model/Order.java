package org.zafu.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.zafu.orderservice.dto.request.PaymentMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Order extends EntityBase<Integer> {
    private Integer userId;
    @Column(nullable = false, unique = true)
    private String orderCode;
    @Column(nullable = false)
    private double totalAmount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String province;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String ward;
    private String notes;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();


    @PrePersist
    public void generateOrderCode(){
        if(orderCode == null){
            this.orderCode = "ORD-"+ UUID.randomUUID()
                    .toString()
                    .substring(0,8)
                    .toUpperCase();
        }
    }
}

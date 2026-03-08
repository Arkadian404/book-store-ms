package org.zafu.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_payment_session_id", columnNames = "session_id"),
                @UniqueConstraint(name = "uk_payment_order_type", columnNames = {"order_id", "type"}),
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer orderId;
    @Column(nullable = false)
    private Integer userId;
    @Column(unique = true, nullable = false)
    private String sessionId;
    @Column(nullable = false)
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod type;

    @CreationTimestamp
    private LocalDateTime createdAt;

}

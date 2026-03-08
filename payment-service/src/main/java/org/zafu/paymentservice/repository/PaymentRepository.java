package org.zafu.paymentservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.paymentservice.model.Payment;
import org.zafu.paymentservice.model.PaymentMethod;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    boolean existsBySessionId(String sessionId);
    boolean existsByOrderIdAndType(Integer orderId, PaymentMethod paymentMethod);
}

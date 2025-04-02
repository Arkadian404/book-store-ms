package org.zafu.paymentservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.paymentservice.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}

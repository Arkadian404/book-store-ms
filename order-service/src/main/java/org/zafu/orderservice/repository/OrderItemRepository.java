package org.zafu.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.orderservice.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}

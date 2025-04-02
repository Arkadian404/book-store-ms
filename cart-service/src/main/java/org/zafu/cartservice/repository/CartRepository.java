package org.zafu.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.cartservice.model.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(Integer userId);
}

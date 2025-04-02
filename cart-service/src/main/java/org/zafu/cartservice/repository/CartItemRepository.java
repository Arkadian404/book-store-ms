package org.zafu.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.cartservice.model.CartItem;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartIdAndBookId(Integer cartId, Integer bookId);
    void deleteAllByCartId(Integer cartId);
}

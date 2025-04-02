package org.zafu.cartservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem extends EntityBase<Integer> {

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    @Column(nullable = false)
    private Integer bookId;
    private int quantity;

}

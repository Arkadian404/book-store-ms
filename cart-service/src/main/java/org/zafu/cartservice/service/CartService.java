package org.zafu.cartservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zafu.cartservice.client.BookClient;
import org.zafu.cartservice.dto.request.AddCartItemRequest;
import org.zafu.cartservice.dto.request.UpdateCartItemRequest;
import org.zafu.cartservice.dto.response.CartItemResponse;
import org.zafu.cartservice.dto.response.CartResponse;
import org.zafu.cartservice.exception.AppException;
import org.zafu.cartservice.exception.ErrorCode;
import org.zafu.cartservice.mapper.CartItemMapper;
import org.zafu.cartservice.mapper.CartMapper;
import org.zafu.cartservice.model.Cart;
import org.zafu.cartservice.model.CartItem;
import org.zafu.cartservice.repository.CartItemRepository;
import org.zafu.cartservice.repository.CartRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CART-SERVICE")
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookClient bookClient;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @Transactional
    public CartResponse getCart(Integer userId){
        Cart cart = getOrCreateCart(userId);
        CartResponse response = cartMapper.toCartResponse(cart);
        response.setItems(cart.getCartItems()
                .stream()
                .map(item -> cartItemMapper.toCartItemResponse(item, bookClient))
                .toList());
        return response;
    }

    public List<CartItemResponse> getCartItems(Integer userId){
        Cart cart = getOrCreateCart(userId);
        return cart.getCartItems()
                .stream()
                .map(item -> cartItemMapper.toCartItemResponse(item, bookClient))
                .toList();
    }



    @Transactional
    public void addItemToCart(Integer userId, AddCartItemRequest request){
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findByCartIdAndBookId(cart.getId(), request.getBookId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setBookId(request.getBookId());
                    newItem.setQuantity(0);
                    return newItem;
                });
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void updateCartItemQuantity(Integer userId, Integer bookId, UpdateCartItemRequest request){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findByCartIdAndBookId(cart.getId(), bookId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeItemFromCart(Integer userId, Integer bookId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findByCartIdAndBookId(cart.getId(), bookId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Integer userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        cartItemRepository.deleteAllByCartId(cart.getId());
    }

    private Cart getOrCreateCart(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }
}

package org.zafu.cartservice.mapper;

import org.mapstruct.Mapper;
import org.zafu.cartservice.dto.response.CartResponse;
import org.zafu.cartservice.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(Cart cart);
}

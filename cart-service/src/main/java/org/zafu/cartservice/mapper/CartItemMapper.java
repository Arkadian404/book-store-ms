package org.zafu.cartservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.zafu.cartservice.client.BookClient;
import org.zafu.cartservice.dto.ApiResponse;
import org.zafu.cartservice.dto.response.BookResponse;
import org.zafu.cartservice.dto.response.CartItemResponse;
import org.zafu.cartservice.exception.AppException;
import org.zafu.cartservice.exception.ErrorCode;
import org.zafu.cartservice.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemResponse toCartItemResponse(CartItem cartItem, @Context BookClient bookClient);

    @AfterMapping
    default void fillBookInfo(@MappingTarget CartItemResponse response, CartItem cartItem, @Context BookClient bookClient){
        ApiResponse<BookResponse> api = bookClient.getBookById(cartItem.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        BookResponse bookResponse = api.getResult();
        response.setBookPrice(bookResponse.getPrice());
        response.setBookTitle(bookResponse.getTitle());
        response.setBookImageUrl(bookResponse.getImageUrl());
    }
}

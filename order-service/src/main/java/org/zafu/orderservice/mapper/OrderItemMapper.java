package org.zafu.orderservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.zafu.orderservice.client.BookClient;
import org.zafu.orderservice.dto.request.StripeItemRequest;
import org.zafu.orderservice.dto.response.BookResponse;
import org.zafu.orderservice.dto.response.OrderItemResponse;
import org.zafu.orderservice.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    StripeItemRequest toPaymentItemRequest(OrderItem orderItem, @Context BookClient bookClient);
    OrderItemResponse toOrderItemResponse(OrderItem orderItem, @Context BookClient bookClient);


    @AfterMapping
    default void fillBookInfoPayment(@MappingTarget StripeItemRequest request, OrderItem orderItem, @Context BookClient bookClient){
        BookResponse bookResponse = bookClient.getBookById(orderItem.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"))
                .getResult();
        request.setBookTitle(bookResponse.getTitle());
        request.setBookImageUrl(bookResponse.getImageUrl());
    }

    @AfterMapping
    default void fillBookInfo(@MappingTarget OrderItemResponse request, OrderItem orderItem, @Context BookClient bookClient){
        BookResponse bookResponse = bookClient.getBookById(orderItem.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"))
                .getResult();
        request.setBookTitle(bookResponse.getTitle());
        request.setBookImageUrl(bookResponse.getImageUrl());
    }
}

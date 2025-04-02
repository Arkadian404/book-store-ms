package org.zafu.orderservice.mapper;

import org.mapstruct.*;
import org.zafu.orderservice.client.BookClient;
import org.zafu.orderservice.dto.request.CreateOrderRequest;
import org.zafu.orderservice.dto.request.OrderConfirmation;
import org.zafu.orderservice.dto.request.OrderItemConfirmation;
import org.zafu.orderservice.dto.response.OrderResponse;
import org.zafu.orderservice.model.Order;

import java.util.List;
import java.util.StringJoiner;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    Order toOrder(CreateOrderRequest request);
    @Mapping(target = "items", source = "items")
    @Mapping(target = "createdDate", source = "createdDate")
    OrderResponse toOrderResponse(Order order, @Context BookClient bookClient);

    @Mapping(target = "items", source = "items", ignore = true)
    @Mapping(target = "address", source = "address", ignore = true)
    OrderConfirmation toOrderConfirmation(OrderResponse order);

    @AfterMapping
    default void setOrderConfirmationItems(@MappingTarget OrderConfirmation confirmation, OrderResponse response){
        StringJoiner joiner = new StringJoiner(", ");
        joiner
                .add(response.getAddress())
                .add(response.getWard())
                .add(response.getDistrict())
                .add(response.getProvince());
        confirmation.setAddress(joiner.toString());
        List<OrderItemConfirmation> items = response.getItems().stream()
                .map(item -> {
                    OrderItemConfirmation orderItemConfirmation = new OrderItemConfirmation();
                    orderItemConfirmation.setBookTitle(item.getBookTitle());
                    orderItemConfirmation.setBookQuantity(item.getBookQuantity());
                    orderItemConfirmation.setBookPrice(item.getBookPrice());
                    orderItemConfirmation.setBookImageUrl(item.getBookImageUrl());
                    return orderItemConfirmation;
                })
                .toList();
        confirmation.setItems(items);
    }

}

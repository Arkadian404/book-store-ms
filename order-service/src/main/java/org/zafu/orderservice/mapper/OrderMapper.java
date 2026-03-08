package org.zafu.orderservice.mapper;

import org.mapstruct.*;
import org.zafu.orderservice.client.BookClient;
import org.zafu.orderservice.dto.request.CreateOrderRequest;
import org.zafu.orderservice.dto.request.OrderConfirmation;
import org.zafu.orderservice.dto.request.OrderItemConfirmation;
import org.zafu.orderservice.dto.response.InternalOrderResponse;
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

    @Mapping(target = "items", source = "items")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "fullAddress", ignore = true)
    InternalOrderResponse toInternalOrderResponse(Order order, @Context BookClient bookClient);

    @AfterMapping
    default void setInternalOrderFullAddress(@MappingTarget InternalOrderResponse response, Order order) {
        response.setFullAddress(toFullAddress(
                order.getAddress(),
                order.getWard(),
                order.getDistrict(),
                order.getProvince()
        ));
    }

    @AfterMapping
    default void setOrderConfirmationItems(@MappingTarget OrderConfirmation confirmation, OrderResponse response){
        confirmation.setAddress(toFullAddress(
                response.getAddress(),
                response.getWard(),
                response.getDistrict(),
                response.getProvince()
        ));
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
    default String toFullAddress(String address, String ward, String district, String province) {
        StringJoiner joiner = new StringJoiner(", ");
        addIfPresent(joiner, address);
        addIfPresent(joiner, ward);
        addIfPresent(joiner, district);
        addIfPresent(joiner, province);
        return joiner.toString();
    }

    default void addIfPresent(StringJoiner joiner, String value) {
        if (value != null && !value.isBlank()) {
            joiner.add(value);
        }
    }

}

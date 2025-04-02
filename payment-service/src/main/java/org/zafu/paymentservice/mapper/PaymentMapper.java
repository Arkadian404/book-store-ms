package org.zafu.paymentservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.zafu.paymentservice.dto.request.OrderConfirmation;
import org.zafu.paymentservice.dto.request.OrderItemConfirmation;
import org.zafu.paymentservice.dto.request.PaymentRequest;
import org.zafu.paymentservice.dto.response.OrderResponse;
import org.zafu.paymentservice.model.Payment;

import java.util.List;
import java.util.StringJoiner;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentRequest paymentRequest);


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

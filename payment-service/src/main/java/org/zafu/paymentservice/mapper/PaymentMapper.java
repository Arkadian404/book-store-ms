package org.zafu.paymentservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.zafu.paymentservice.dto.request.OrderConfirmation;
import org.zafu.paymentservice.dto.request.OrderItemConfirmation;
import org.zafu.paymentservice.dto.request.PaymentRequest;
import org.zafu.paymentservice.dto.response.InternalOrderItemResponse;
import org.zafu.paymentservice.dto.response.InternalOrderResponse;
import org.zafu.paymentservice.dto.response.OrderItemResponse;
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

    @Mapping(target = "items", source = "items", ignore = true)
    @Mapping(target = "address", source = "address", ignore = true)
    OrderConfirmation toOrderConfirmation(InternalOrderResponse order);

    @AfterMapping
    default void setOrderConfirmationItems(@MappingTarget OrderConfirmation confirmation, OrderResponse response){
        confirmation.setAddress(toFullAddress(
                response.getAddress(),
                response.getWard(),
                response.getDistrict(),
                response.getProvince()
        ));
        confirmation.setItems(toOrderItemConfirmations(response.getItems()));
    }

    @AfterMapping
    default void setOrderConfirmationItems(@MappingTarget OrderConfirmation confirmation, InternalOrderResponse response){
        confirmation.setAddress(toFullAddress(
                response.getAddress(),
                response.getWard(),
                response.getDistrict(),
                response.getProvince()
        ));
        confirmation.setItems(toInternalOrderItemConfirmations(response.getItems()));
    }


    default String toFullAddress(String address, String ward, String district, String province) {
        StringJoiner joiner = new StringJoiner(", ");
        joiner
                .add(address)
                .add(ward)
                .add(district)
                .add(province);
        return joiner.toString();
    }

    default List<OrderItemConfirmation> toOrderItemConfirmations(List<OrderItemResponse> items) {
        return items.stream()
                .map(item -> toOrderItemConfirmation(
                        item.getBookTitle(),
                        item.getBookQuantity(),
                        item.getBookPrice(),
                        item.getBookImageUrl()
                ))
                .toList();
    }

    default List<OrderItemConfirmation> toInternalOrderItemConfirmations(List<InternalOrderItemResponse> items) {
        return items.stream()
                .map(item -> toOrderItemConfirmation(
                        item.getBookTitle(),
                        item.getBookQuantity(),
                        item.getBookPrice(),
                        item.getBookImageUrl()
                ))
                .toList();
    }

    default OrderItemConfirmation toOrderItemConfirmation(String title, int quantity, double price, String imageUrl) {
        OrderItemConfirmation orderItemConfirmation = new OrderItemConfirmation();
        orderItemConfirmation.setBookTitle(title);
        orderItemConfirmation.setBookQuantity(quantity);
        orderItemConfirmation.setBookPrice(price);
        orderItemConfirmation.setBookImageUrl(imageUrl);
        return orderItemConfirmation;
    }
}

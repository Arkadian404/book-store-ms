package org.zafu.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.zafu.orderservice.dto.request.OrderConfirmation;
import org.zafu.orderservice.dto.response.OrderResponse;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER-PRODUCER")
public class OrderProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentConfirmation(OrderConfirmation orderConfirmation){
        log.info("Sending payment confirmation to kafka topic");
        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)
                .setHeader(KafkaHeaders.TOPIC, "payment-confirmation-topic")
                .build();
        kafkaTemplate.send(message);
    }
}

package org.zafu.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic paymentConfirmation(){
        return TopicBuilder.name("payment-confirmation-topic")
                .partitions(2)
                .build();
    }

    @Bean
    public NewTopic orderConfirmation(){
        return TopicBuilder.name("order-confirmation-topic")
                .partitions(2)
                .build();
    }
}

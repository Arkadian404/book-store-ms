package org.zafu.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic verificationTopic(){
        return TopicBuilder
                .name("verification-topic")
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic resetPasswordTopic(){
        return TopicBuilder
                .name("reset-password-topic")
                .partitions(2)
                .build();
    }

    @Bean
    public NewTopic passwordChangedTopic(){
        return TopicBuilder
                .name("password-changed-topic")
                .partitions(2)
                .build();
    }
}

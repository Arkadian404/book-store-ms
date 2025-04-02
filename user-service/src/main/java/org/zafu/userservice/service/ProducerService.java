package org.zafu.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.zafu.userservice.dto.request.EmailVerificationRequest;
import org.zafu.userservice.dto.request.PasswordChangedNotification;
import org.zafu.userservice.dto.request.PasswordResetRequest;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCER-SERVICE")
public class ProducerService {
    private final KafkaTemplate<String, Object> template;

    public void sendEmailVerificationRequest(EmailVerificationRequest request){
        log.info(String.format("Sending verification request %s", request));
        Message<EmailVerificationRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "verification-topic")
                .build();
        template.send(message);
    }

    public void sendPasswordResetRequest(PasswordResetRequest request){
        log.info(String.format("Sending reset password request %s", request));
        Message<PasswordResetRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "reset-password-topic")
                .build();
        template.send(message);
    }

    public void sendPasswordChangedNotification(PasswordChangedNotification request){
        log.info(String.format("Sending password changed notification %s", request));
        Message<PasswordChangedNotification> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "password-changed-topic")
                .build();
        template.send(message);
    }

}

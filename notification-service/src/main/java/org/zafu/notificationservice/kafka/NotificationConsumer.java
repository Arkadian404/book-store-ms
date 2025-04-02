package org.zafu.notificationservice.kafka;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.zafu.notificationservice.kafka.order.OrderConfirmation;
import org.zafu.notificationservice.kafka.user.EmailVerificationRequest;
import org.zafu.notificationservice.kafka.user.PasswordChangedNotification;
import org.zafu.notificationservice.kafka.user.PasswordResetRequest;
import org.zafu.notificationservice.service.EmailService;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "CONSUMER-TOPIC")
public class NotificationConsumer {
    private final EmailService service;
    private final EmailService emailService;

    @RetryableTopic
    @KafkaListener(topics = "verification-topic", groupId = "email-group")
    public void listenEmailVerificationRequest(EmailVerificationRequest request, Acknowledgment acknowledgment){
        try{
            log.info("Received message {} ", request);
            emailService.sendEmailVerification(
                    request.getUsername(),
                    request.getEmail(),
                    request.getVerificationToken()
            );
            acknowledgment.acknowledge();
        }catch (MessagingException e) {
            log.info("Error when sending email verification request {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "reset-password-topic", groupId = "email-group")
    public void listenResetPassword(PasswordResetRequest request, Acknowledgment acknowledgment){
        log.info("Received message {}", request);
        try {
            emailService.sendPasswordResetEmail(
                    request.getUsername(),
                    request.getEmail(),
                    request.getResetToken()
            );
            acknowledgment.acknowledge();
        } catch (MessagingException e) {
            log.error("Error when sending reset password email {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "password-changed-topic", groupId = "email-group")
    public void listenPasswordChangedNotification(PasswordChangedNotification request, Acknowledgment acknowledgment){
        log.info("Received message {}", request);
        try {
            emailService.sendPasswordChangedNotification(
                    request.getUsername(),
                    request.getEmail()
            );
            acknowledgment.acknowledge();
        } catch (MessagingException e) {
            log.error("Error when sending password changed notification email {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "payment-confirmation-topic", groupId = "email-group")
    public void listenPaymentConfirmation(OrderConfirmation request, Acknowledgment acknowledgment){
        log.info("Received message {}", request);
        try {
            emailService.sendPaymentConfirmation(request);
            acknowledgment.acknowledge();
        }catch (MessagingException e){
            log.error("Error when sending payment confirmation email {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @DltHandler
    public void listenDLT(EmailVerificationRequest request){
        log.info("Received message from DLT {} ", request);
    }
}

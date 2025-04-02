package org.zafu.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.zafu.notificationservice.kafka.order.OrderConfirmation;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-SERVICE")
public class EmailService {
    private final SpringTemplateEngine engine;
    private final JavaMailSender sender;
    private final String from = "zafuog@noreply.com";


    @Async
    public void sendEmailVerification(String username, String email, String token) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setFrom(from);
        final String type = EmailType.EMAIL_VERIFICATION.getType();
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("email", email);
        variables.put("token", token);
        Context context = new Context();
        context.setVariables(variables);
        helper.setSubject(EmailType.EMAIL_VERIFICATION.getSubject());
        String htmlTemplate = engine.process(type, context);
        helper.setText(htmlTemplate, true);
        helper.setTo(email);
        sender.send(mimeMessage);
        log.info(String.format("Email successfully send to %s with template %s", email, type));
    }

    @Async
    public void sendPasswordResetEmail(String username, String email, String token) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setFrom(from);
        final String type = EmailType.PASSWORD_RESET.getType();
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("email", email);
        variables.put("token", token);
        Context context = new Context();
        context.setVariables(variables);
        String htmlTemplate = engine.process(type, context);
        helper.setText(htmlTemplate, true);
        helper.setSubject(EmailType.PASSWORD_RESET.getSubject());
        helper.setTo(email);
        sender.send(mimeMessage);
    }

    @Async
    public void sendPasswordChangedNotification(String username, String email) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setFrom(from);
        final String type = EmailType.PASSWORD_CHANGED.getType();
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("email", email);
        Context context = new Context();
        context.setVariables(variables);
        String htmlTemplate = engine.process(type, context);
        helper.setText(htmlTemplate, true);
        helper.setSubject(EmailType.PASSWORD_CHANGED.getSubject());
        helper.setTo(email);
        sender.send(mimeMessage);
    }

    @Async
    public void sendPaymentConfirmation(OrderConfirmation confirmation) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setFrom(from);
        final String type = EmailType.PAYMENT_CONFIRMATION.getType();
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderCode", confirmation.getOrderCode());
        variables.put("firstname", confirmation.getFirstname());
        variables.put("lastname", confirmation.getLastname());
        variables.put("status", confirmation.getStatus());
        variables.put("createdDate", confirmation.getCreatedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        variables.put("totalAmount", confirmation.getTotalAmount());
        variables.put("address", confirmation.getAddress());
        variables.put("items", confirmation.getItems());
        Context context = new Context();
        context.setVariables(variables);
        String htmlTemplate = engine.process(type, context);
        helper.setText(htmlTemplate, true);
        helper.setSubject(EmailType.PAYMENT_CONFIRMATION.getSubject()+ " - #" + confirmation.getOrderCode());
        helper.setTo(confirmation.getEmail());
        sender.send(mimeMessage);
    }

}


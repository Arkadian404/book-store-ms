package org.zafu.notificationservice.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {
    EMAIL_VERIFICATION("email-verification.html", "Email verification successfully processed"),
    PASSWORD_RESET("reset-password.html", "Password reset action"),
    PASSWORD_CHANGED("password-changed.html", "Password changed successfully"),
    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment confirmation")
    ;

    private final String type;
    private final String subject;
}

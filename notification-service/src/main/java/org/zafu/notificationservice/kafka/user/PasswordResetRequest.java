package org.zafu.notificationservice.kafka.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    private String username;
    private String email;
    private String resetToken;
}

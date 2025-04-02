package org.zafu.userservice.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationRequest {
    private String username;
    private String email;
    private String verificationToken;
}

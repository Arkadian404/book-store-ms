package org.zafu.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_uq", columnNames = "username"),
                @UniqueConstraint(name = "email_uq", columnNames = "email"),
                @UniqueConstraint(name = "phone_uq", columnNames = "phone"),
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder.Default
    private boolean isActivated = false;
    @Column(length = 500)
    private String activationToken;
    @Column(length = 500)
    private String resetPasswordToken;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}

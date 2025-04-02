package org.zafu.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(111, "User not found", HttpStatus.BAD_REQUEST),
    USER_IS_ACTIVATED(111, "User is activated already", HttpStatus.BAD_REQUEST),
    INVALID_USER_VERIFICATION_TOKEN(111, "User's verification token is invalid", HttpStatus.BAD_REQUEST),
    INVALID_USER_RESET_TOKEN(111, "User's reset password token is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_UNMATCHED(105, "Password unmatched", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Unauthorized", HttpStatus.FORBIDDEN),
    PAGE_OR_SIZE_MUST_BE_VALID(115, "Page or size must be valid", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}

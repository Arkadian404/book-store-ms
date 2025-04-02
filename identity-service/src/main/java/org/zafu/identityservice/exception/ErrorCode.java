package org.zafu.identityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(111, "User not found in system", HttpStatus.BAD_REQUEST),
    UNABLE_TO_VERIFY_TOKEN(503, "Unable to verify token", HttpStatus.SERVICE_UNAVAILABLE),
    INVALID_CREDENTIALS(111,"Username or password incorrect", HttpStatus.BAD_REQUEST),
    UNABLE_TO_LOGOUT(149,"Unable to logout" ,HttpStatus.BAD_REQUEST),
    UNABLE_TO_REFRESH_TOKEN(141, "Unable to refresh token", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}

package org.zafu.cartservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(111, "User not found", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(147, "Cart not found", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Unauthorized", HttpStatus.FORBIDDEN),
    CART_ITEM_NOT_FOUND(145,"Cart item not found" ,HttpStatus.BAD_REQUEST ),
    BOOK_NOT_FOUND(258,"Book not found" , HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}

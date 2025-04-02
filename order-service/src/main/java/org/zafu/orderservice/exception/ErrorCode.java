package org.zafu.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(111, "User not found", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Unauthorized", HttpStatus.FORBIDDEN),
    CART_NOT_FOUND(113, "Cart not found", HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND(117,"Book not found" ,HttpStatus.BAD_REQUEST ),
    STOCK_NOT_ENOUGH(118, "Stock not enough", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(114, "Order not found",HttpStatus.BAD_REQUEST),
    PAGE_OR_SIZE_MUST_BE_VALID(123, "Page or size isn't valid", HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED(115, "Payment failed", HttpStatus.BAD_REQUEST),
    PAYMENT_METHOD_NOT_SUPPORTED(119, "Payment method not supported",HttpStatus.BAD_REQUEST),
    CART_IS_EMPTY(110, "Cart is empty",HttpStatus.BAD_REQUEST );

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}

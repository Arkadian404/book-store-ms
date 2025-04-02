package org.zafu.bookservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UPLOAD_IMAGE_FAILED(999, "Upload image failed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You have no rights for accessing this resource", HttpStatus.FORBIDDEN),
    AUTHOR_NOT_EXISTED(111, "Author is not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(111, "Category is not existed", HttpStatus.BAD_REQUEST),
    PUBLISHER_NOT_EXISTED(111, "Publisher is not existed", HttpStatus.BAD_REQUEST),
    BOOK_NOT_EXISTED(111, "Book is not existed", HttpStatus.BAD_REQUEST),
    INVALID_KEY_FILTER(113, "Invalid key filter", HttpStatus.BAD_REQUEST),
    INVALID_OPERATOR_FILTER(114, "Invalid operator filter", HttpStatus.BAD_REQUEST),
    PAGE_OR_SIZE_MUST_BE_VALID(115, "Page or size must be valid", HttpStatus.BAD_REQUEST),
    FILE_FORMAT_INCORRECT(415, "File format incorrect", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    FILE_READ_ERROR(369, "Error when read file", HttpStatus.BAD_REQUEST),
    FILE_WRITE_ERROR(963, "Error when write file", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_STOCK(258, "Not enough stock quantity", HttpStatus.BAD_REQUEST),
    INVALID_BOOK_REQUEST(101,"Invalid book request" ,HttpStatus.BAD_REQUEST );

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}

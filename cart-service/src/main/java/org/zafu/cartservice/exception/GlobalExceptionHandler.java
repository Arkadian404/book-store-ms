package org.zafu.cartservice.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zafu.cartservice.dto.ErrorResponse;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j(topic = "GLOBAL HANDLER")
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
//                .error(errors.size() > 1 ? String.valueOf(errors) : errors.get(0))
                .error(errors.get(0))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @ExceptionHandler(AppException.class)
    ResponseEntity<ErrorResponse> handlerAppException(AppException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(exception.getErrorCode().getHttpStatus().value())
                .error(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(errorResponse);
    }


}

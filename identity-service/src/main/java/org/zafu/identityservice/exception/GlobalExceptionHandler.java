package org.zafu.identityservice.exception;



import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zafu.identityservice.dto.ErrorResponse;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j(topic = "GLOBAL HANDLER")
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
//        log.info("Catching exception: {}", ex.getMessage());
//
//        // Tùy theo môi trường: có thể thêm correlationId nếu có
//        ErrorResponse response = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .error("Internal Server Error")
//                .path(request.getRequestURI())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }

//    @ExceptionHandler(AppException.class)
//    ResponseEntity<ApiResponse<?>> handlerAppException(AppException ae) {
//        return ResponseEntity.status(ae.getErrorCode().getHttpStatus())
//                .body(ApiResponse.builder()
//                        .code(ae.getErrorCode().getCode())
//                        .message(ae.getMessage())
//                        .build());
//    }

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

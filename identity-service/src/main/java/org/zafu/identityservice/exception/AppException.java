package org.zafu.identityservice.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppException extends RuntimeException{
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

package com.example.prepaiddata.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class VoucherExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleAnyException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof VoucherAlreadyAppliedException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof VoucherAlreadyExistsException) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), status);
    }

}

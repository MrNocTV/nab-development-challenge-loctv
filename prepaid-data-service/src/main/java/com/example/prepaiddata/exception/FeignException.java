package com.example.prepaiddata.exception;

public class FeignException extends RuntimeException {
    public FeignException(String message) {
        super(message);
    }
}

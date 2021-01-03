package com.example.prepaiddata.exception;

public class VoucherAlreadyExistsException extends RuntimeException {
    public VoucherAlreadyExistsException(String message) {
        super(message);
    }
}

package com.example.prepaiddata.exception;

public class VoucherNotFoundException extends RuntimeException {
    public VoucherNotFoundException(String message) {
        super(message);
    }
}

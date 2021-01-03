package com.example.prepaiddata.exception;

public class VoucherAlreadyAppliedException extends RuntimeException {
    public VoucherAlreadyAppliedException(String message) {
        super(message);
    }
}

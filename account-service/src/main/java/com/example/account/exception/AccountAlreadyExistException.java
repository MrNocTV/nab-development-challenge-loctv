package com.example.account.exception;

public class AccountAlreadyExistException extends RuntimeException {

    public AccountAlreadyExistException(String message) {
        super(message);
    }

}

package com.bankapp.bank_backend.exception;

public class UserInvalidException extends RuntimeException {

    public UserInvalidException(String message) {
        super(message);
    }
}

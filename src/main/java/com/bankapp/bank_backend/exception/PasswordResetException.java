package com.bankapp.bank_backend.exception;

public class PasswordResetException extends RuntimeException {

	public PasswordResetException(String message) {
        super(message);
    }

    public PasswordResetException(String message, Throwable cause) {
        super(message, cause);
    }
}

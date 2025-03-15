package com.bankapp.bank_backend.exception;

public class InvalidTokenException extends Exception {

	public InvalidTokenException(String message) {
        super(message);
    }
}

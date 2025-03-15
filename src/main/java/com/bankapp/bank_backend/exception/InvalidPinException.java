package com.bankapp.bank_backend.exception;

public class InvalidPinException extends RuntimeException {

	public InvalidPinException(String message) {
        super(message);
    }
}

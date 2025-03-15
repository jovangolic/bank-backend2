package com.bankapp.bank_backend.exception;

public class InvalidAmountException extends RuntimeException {

	public InvalidAmountException(String message) {
        super(message);
    }
}

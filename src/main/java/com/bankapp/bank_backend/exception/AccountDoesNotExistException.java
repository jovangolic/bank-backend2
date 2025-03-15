package com.bankapp.bank_backend.exception;

public class AccountDoesNotExistException extends RuntimeException {

	public AccountDoesNotExistException(String message) {
        super(message);
    }
}

package com.bankapp.bank_backend.exception;

public class InvalidOtpException extends RuntimeException {

	public InvalidOtpException(String msg) {
        super(msg);
    }
}

package com.bankapp.bank_backend.exception;

public class OtpRetryLimitExceededException extends RuntimeException {

	public OtpRetryLimitExceededException(String message) {
        super(message);
    }
}

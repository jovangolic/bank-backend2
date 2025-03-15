package com.bankapp.bank_backend.dto;

public record PinUpdateRequest(String accountNumber, String oldPin, String newPin, String password) {

}

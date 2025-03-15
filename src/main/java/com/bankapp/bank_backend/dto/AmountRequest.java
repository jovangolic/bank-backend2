package com.bankapp.bank_backend.dto;

public record AmountRequest(String accountNumber, String pin, double amount) {

}

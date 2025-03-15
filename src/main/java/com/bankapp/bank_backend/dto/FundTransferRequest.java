package com.bankapp.bank_backend.dto;

public record FundTransferRequest(String sourceAccountNumber, String targetAccountNumber, double amount, String pin) {

}

package com.bankapp.bank_backend.dto;

public record ResetPasswordRequest(String identifier, String resetToken, String newPassword) {

}

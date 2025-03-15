package com.bankapp.bank_backend.service;

import org.springframework.http.ResponseEntity;

import com.bankapp.bank_backend.dto.OtpRequest;
import com.bankapp.bank_backend.dto.OtpVerificationRequest;
import com.bankapp.bank_backend.dto.ResetPasswordRequest;
import com.bankapp.bank_backend.model.User;



public interface AuthService {

	public String generatePasswordResetToken(User user);

    public boolean verifyPasswordResetToken(String token, User user);

    public void deletePasswordResetToken(String token);

    public ResponseEntity<String> sendOtpForPasswordReset(OtpRequest otpRequest);

    public ResponseEntity<String> verifyOtpAndIssueResetToken(OtpVerificationRequest otpVerificationRequest);

    public ResponseEntity<String> resetPassword(ResetPasswordRequest resetPasswordRequest);
}

package com.bankapp.bank_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.bankapp.bank_backend.dto.LoginRequest;
import com.bankapp.bank_backend.dto.OtpRequest;
import com.bankapp.bank_backend.dto.OtpVerificationRequest;
import com.bankapp.bank_backend.exception.InvalidTokenException;
import com.bankapp.bank_backend.model.User;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	public ResponseEntity<String> registerUser(User user);

    public ResponseEntity<String> login(LoginRequest loginRequest, HttpServletRequest request)
            throws InvalidTokenException;

    public ResponseEntity<String> generateOtp(OtpRequest otpRequest);

    public ResponseEntity<String> verifyOtpAndLogin(OtpVerificationRequest otpVerificationRequest)
            throws InvalidTokenException;

    public ResponseEntity<String> updateUser(User user);

    public ModelAndView logout(String token) throws InvalidTokenException;

    public boolean resetPassword(User user, String newPassword);

    public User saveUser(User user);

    public User getUserByIdentifier(String identifier);

    public User getUserByAccountNumber(String accountNo);

    public User getUserByEmail(String email);
	
}

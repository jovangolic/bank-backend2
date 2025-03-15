package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.dto.AccountResponse;
import com.bankapp.bank_backend.dto.UserResponse;

public interface DashboardService {

	UserResponse getUserDetails(String accountNumber);
    AccountResponse getAccountDetails(String accountNumber);
}

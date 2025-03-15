package com.bankapp.bank_backend.dto;
import com.bankapp.bank_backend.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

	private String accountNumber;
    private double balance;
    private String accountType;
    private String branch;
    private String ifscCode;

    public AccountResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.branch = account.getBranch();
        this.ifscCode = account.getIfscCode();
    }
}

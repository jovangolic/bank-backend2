package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.model.Account;
import com.bankapp.bank_backend.model.User;

public interface AccountService {

	public Account createAccount(User user);
	public boolean isPinCreated(String accountNumber) ;
	public void createPin(String accountNumber, String password, String pin) ;
	public void updatePin(String accountNumber, String oldPIN, String password, String newPIN);
	public void cashDeposit(String accountNumber, String pin, double amount);
	public void cashWithdrawal(String accountNumber, String pin, double amount);
	public void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String pin, double amount);
}

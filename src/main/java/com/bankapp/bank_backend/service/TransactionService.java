package com.bankapp.bank_backend.service;

import java.util.List;

import com.bankapp.bank_backend.dto.TransactionDTO;



public interface TransactionService {

	List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber);
}

package com.bankapp.bank_backend.mapper;

import org.springframework.stereotype.Component;

import com.bankapp.bank_backend.dto.TransactionDTO;
import com.bankapp.bank_backend.model.Transaction;

@Component
public class TransactionMapper {

	public TransactionDTO toDto(Transaction transaction) {
        return new TransactionDTO(transaction);
    }
}

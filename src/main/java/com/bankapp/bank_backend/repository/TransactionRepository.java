package com.bankapp.bank_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.bank_backend.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	/**
	 *Ove donje crtice _ se koriste za navigaciju kroz ugnjezdene ili povezane entitete u okviru JPA specifikacije.
	 *Dakle, ovaj deo naziva metode pokazuje kako se navigira kroz entitet Transaction do njegovih povezanih entiteta Account,
	 *a zatim se vrsi pretraga na osnovu vrednosti polja accountNumber unutar povezanog entiteta.
	 */
	List<Transaction> findBySourceAccount_AccountNumberOrTargetAccount_AccountNumber(String sourceAccountNumber, String targetAccountNumber);
}

package com.bankapp.bank_backend.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankapp.bank_backend.exception.FundTransferException;
import com.bankapp.bank_backend.exception.InsufficientBalanceException;
import com.bankapp.bank_backend.exception.InvalidAmountException;
import com.bankapp.bank_backend.exception.InvalidPinException;
import com.bankapp.bank_backend.exception.NotFoundException;
import com.bankapp.bank_backend.exception.UnauthorizedException;
import com.bankapp.bank_backend.model.Account;
import com.bankapp.bank_backend.model.Transaction;
import com.bankapp.bank_backend.model.TransactionType;
import com.bankapp.bank_backend.model.User;
import com.bankapp.bank_backend.repository.AccountRepository;
import com.bankapp.bank_backend.repository.TransactionRepository;
import com.bankapp.bank_backend.util.ApiMessages;


import lombok.val;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
	
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Account createAccount(User user) {
		val account = new Account();
		account.setAccountNumber(generateUniqueAccountNumber());
		account.setBalance(0.0);
		account.setUser(user);
		return accountRepository.save(account);
	}

	@Override
	public boolean isPinCreated(String accountNumber) {
		val account = accountRepository.findByAccountNumber(accountNumber);
		if(account == null) {
			throw new NotFoundException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
		}
		return account.getPin() != null;
	}

	@Override
	public void createPin(String accountNumber, String password, String pin) {
		//poziva se metoda validatePasword
		validatePassword(accountNumber, password);
		val account = accountRepository.findByAccountNumber(accountNumber);
		if(account.getPin() != null) {
			throw new UnauthorizedException(ApiMessages.PIN_ALREADY_EXISTS.getMessage());
		}
		if(pin == null || pin.isEmpty()) {
			throw new InvalidPinException(ApiMessages.PIN_EMPTY_ERROR.getMessage());
		}
		if(!pin.matches("[0-9]{4}")) {
			throw new InvalidPinException(ApiMessages.PIN_FORMAT_INVALID_ERROR.getMessage());
		}
		//setovanje novog pin koda
		account.setPin(passwordEncoder.encode(pin));
		accountRepository.save(account);
	}

	@Override
	public void updatePin(String accountNumber, String oldPIN, String password, String newPIN) {
		//postavljanje logger-a
		log.info("Updating PIN for account: {}", accountNumber);
		validatePassword(accountNumber, password);
		validatePin(accountNumber, newPIN);
		val account = accountRepository.findByAccountNumber(accountNumber);
		if(newPIN == null || newPIN.isEmpty()) {
			throw new InvalidPinException(ApiMessages.PIN_EMPTY_ERROR.getMessage());
		}
		if(!newPIN.matches("[0-9]{4}")) {
			throw new InvalidPinException(ApiMessages.PIN_FORMAT_INVALID_ERROR.getMessage());
		}
		account.setPin(passwordEncoder.encode(newPIN));
		accountRepository.save(account);
	}

	//metoda za dodavanje novca
	@Override
	public void cashDeposit(String accountNumber, String pin, double amount) {
		validatePin(accountNumber, pin);
		validateAmount(amount);
		val account = accountRepository.findByAccountNumber(accountNumber);
		//trenutna kolicina novca na racunu
		val currentBalance = account.getBalance();
		//nova kolicina novca na racunu
		val newBalance = currentBalance + amount;
		account.setBalance(newBalance);
		accountRepository.save(account);
		//transakcija
		val tranasaction = new Transaction();
		tranasaction.setAmount(amount);
		//tip transakcije
		tranasaction.setTransactionType(TransactionType.CASH_DEPOSIT);
		tranasaction.setTransactionDate(new Date());
		//racun sa koga se prebacuje
		tranasaction.setSourceAccount(account);
		transactionRepository.save(tranasaction);
	}

	//metoda za povlacenje novca
	@Override
	public void cashWithdrawal(String accountNumber, String pin, double amount) {
		validatePin(accountNumber, pin);
		validateAmount(amount);
		val account = accountRepository.findByAccountNumber(accountNumber);
		val currentBlance = account.getBalance();
		//provera tekuceg stanja na racunu
		if(currentBlance < amount) {
			throw new InsufficientBalanceException(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage());
		}
		//novo stanje
		val newBalance = currentBlance - amount;
		account.setBalance(newBalance);
		accountRepository.save(account);
		//transakcija
		val transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setTransactionType(TransactionType.CASH_WITHDRAWAL);
		transaction.setTransactionDate(new Date());
		transaction.setSourceAccount(account);
		transactionRepository.save(transaction);
	}

	@Override
	public void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String pin, double amount) {
		validatePin(targetAccountNumber, pin);
		validateAmount(amount);
		if(sourceAccountNumber.equals(targetAccountNumber)) {
			throw new FundTransferException(ApiMessages.CASH_TRANSFER_SAME_ACCOUNT_ERROR.getMessage());
		}
		val targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
		if(targetAccount == null) {
			throw new NotFoundException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
		}
		val sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
		val sourceBlance = sourceAccount.getBalance();
		if(sourceBlance < amount) {
			throw new InsufficientBalanceException(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage());
		}
		val newSourceBalance = sourceBlance - amount;
		sourceAccount.setBalance(newSourceBalance);
		accountRepository.save(sourceAccount);
		val targetBalance = targetAccount.getBalance();
		val newTargetBalance = targetBalance + amount;
		targetAccount.setBalance(newTargetBalance);
		accountRepository.save(targetAccount);
		val transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setTransactionType(TransactionType.CASH_TRANSFER);
		transaction.setTransactionDate(new Date());
		transaction.setSourceAccount(sourceAccount);
		transaction.setTargetAccount(targetAccount);
		transactionRepository.save(transaction);
	}

	//metoda za generisanje jedinstvenog broja za account
	private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            //Generise UUID kao broj racuna
            accountNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        } while (accountRepository.findByAccountNumber(accountNumber) != null);

        return accountNumber;
    }
	
	//metoda za validaciju pin koda
	private void validatePin(String accountNumber, String pin) {
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        if (account.getPin() == null) {
            throw new UnauthorizedException(ApiMessages.PIN_NOT_CREATED.getMessage());
        }

        if (pin == null || pin.isEmpty()) {
            throw new UnauthorizedException(ApiMessages.PIN_EMPTY_ERROR.getMessage());
        }

        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new UnauthorizedException(ApiMessages.PIN_INVALID_ERROR.getMessage());
        }
    }

	//metoda za validaciju lozinke
    private void validatePassword(String accountNumber, String password) {
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        if (password == null || password.isEmpty()) {
            throw new UnauthorizedException(ApiMessages.PASSWORD_EMPTY_ERROR.getMessage());
        }

        if (!passwordEncoder.matches(password, account.getUser().getPassword())) {
            throw new UnauthorizedException(ApiMessages.PASSWORD_INVALID_ERROR.getMessage());
        }
    }
    
    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_NEGATIVE_ERROR.getMessage());
        }

        if (amount % 100 != 0) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_NOT_MULTIPLE_OF_100_ERROR.getMessage());
        }

        if (amount > 100000) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_EXCEED_100_000_ERROR.getMessage());
        }
    }
}

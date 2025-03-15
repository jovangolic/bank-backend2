package com.bankapp.bank_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.bank_backend.model.Account;
import com.bankapp.bank_backend.model.Token;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Token findByToken(String token);

    Token[] findAllByAccount(Account account);

    void deleteByToken(String token);
}

package com.bankofspain.bankofspain.repository;

import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankAccountRepository
        extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    Optional<BankAccount> findByOwner_Username(String username);

    boolean existsByAccountNumber(String accountNumber);

    @Query(
        value = "SELECT * FROM bank_accounts WHERE account_number = :accountNumber FOR UPDATE",
        nativeQuery = true
    )
    Optional<BankAccount> findByAccountNumberForUpdate(
            @Param("accountNumber") String accountNumber
    );
}
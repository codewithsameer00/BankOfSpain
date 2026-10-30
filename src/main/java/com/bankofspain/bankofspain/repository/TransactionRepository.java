package com.bankofspain.bankofspain.repository;

import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankTransaction;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findByAccountOrderByTransactionDateDesc(
            BankAccount account
    );

    List<BankTransaction> findByTransferReference(
            String transferReference
    );
}

package com.bankofspain.bankofspain.service;

import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankAccount;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankTransaction;

import com.bankofspain.bankofspain.repository.BankAccountRepository;
import com.bankofspain.bankofspain.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public DashboardService(
            BankAccountRepository bankAccountRepository,
            TransactionRepository transactionRepository) {

        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public BankAccount getAccount(String username) {

        BankAccount account = bankAccountRepository
                .findByOwner_Username(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bank account not found for this user."
                        ));

        // Initialize the lazy-loaded customer while the session is open.
        account.getOwner().getFullName();
        account.getOwner().getUsername();

        return account;
    }

    @Transactional(readOnly = true)
    public List<BankTransaction> getTransactions(String username) {

        BankAccount account = bankAccountRepository
                .findByOwner_Username(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bank account not found for this user."
                        ));

        return transactionRepository
                .findByAccountOrderByTransactionDateDesc(account);
    }
}


package com.bankofspain.bankofspain.service;

import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankAccount;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankTransaction;
import com.bankofspain.bankofspain.dto.DepositRequest;
import com.bankofspain.bankofspain.repository.BankAccountRepository;
import com.bankofspain.bankofspain.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DepositService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public DepositService(
            BankAccountRepository bankAccountRepository,
            TransactionRepository transactionRepository) {

        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public String deposit(String username, DepositRequest request) {

        BigDecimal amount = request.getAmount();

        // Validate the deposit amount.
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero."
            );
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException(
                    "Deposit amount cannot have more than two decimal places."
            );
        }

        // Find the logged-in user's account.
        BankAccount userAccount = bankAccountRepository
                .findByOwner_Username(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bank account not found."
                ));

        // Lock the account before changing its balance.
        BankAccount account = bankAccountRepository
                .findByAccountNumberForUpdate(userAccount.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bank account not found."
                ));

        // Verify account ownership.
        if (!account.getOwner().getUsername().equals(username)) {
            throw new IllegalArgumentException(
                    "You are not authorized to deposit into this account."
            );
        }

        // Update the account balance.
        BigDecimal updatedBalance = account.getBalance().add(amount);

        account.setBalance(updatedBalance);

        bankAccountRepository.save(account);

        // Generate a unique transaction reference.
        String reference = UUID.randomUUID().toString();

        // Create the deposit transaction record.
        BankTransaction transaction = new BankTransaction();

        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");
        transaction.setTransferReference(reference);
        transaction.setAccount(account);

        String description = request.getDescription();

        if (description == null || description.isBlank()) {
            description = "Demo account deposit";
        }

        transaction.setDescription(description.trim());

        transactionRepository.save(transaction);

        return reference;
    }
}


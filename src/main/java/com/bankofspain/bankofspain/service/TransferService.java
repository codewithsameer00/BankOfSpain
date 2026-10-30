
package com.bankofspain.bankofspain.service;

import com.bankofspain.bankofspain.dto.TransferRequest;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.AppUser;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankAccount;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankTransaction;

import com.bankofspain.bankofspain.repository.BankAccountRepository;
import com.bankofspain.bankofspain.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransferService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(
            BankAccountRepository bankAccountRepository,
            TransactionRepository transactionRepository) {

        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public String transfer(String senderUsername, TransferRequest request) {

        // Validate transfer amount
        BigDecimal amount = request.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero."
            );
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException(
                    "Transfer amount cannot have more than two decimal places."
            );
        }

        // Find sender's account
        BankAccount senderAccount = bankAccountRepository
                .findByOwner_Username(senderUsername)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Your bank account could not be found."
                ));

        String senderNumber = senderAccount.getAccountNumber();
        String recipientNumber = request.getRecipientAccountNumber();

        // Validate recipient account number
        if (recipientNumber == null || recipientNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Please enter a recipient account number."
            );
        }

        if (senderNumber.equals(recipientNumber.trim())) {
            throw new IllegalArgumentException(
                    "You cannot transfer money to your own account."
            );
        }

        // Check whether recipient exists
        bankAccountRepository.findByAccountNumber(recipientNumber.trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recipient bank account does not exist."
                ));

        // Lock accounts in a consistent order to reduce deadlocks
        String firstNumber = senderNumber.compareTo(recipientNumber.trim()) < 0
                ? senderNumber : recipientNumber.trim();

        String secondNumber = senderNumber.compareTo(recipientNumber.trim()) < 0
                ? recipientNumber.trim() : senderNumber;

        BankAccount firstAccount = bankAccountRepository
                .findByAccountNumberForUpdate(firstNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bank account could not be found."
                ));

        BankAccount secondAccount = bankAccountRepository
                .findByAccountNumberForUpdate(secondNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bank account could not be found."
                ));

        // Identify sender and receiver after locking
        BankAccount sender = firstAccount.getAccountNumber().equals(senderNumber)
                ? firstAccount : secondAccount;

        BankAccount receiver = firstAccount.getAccountNumber().equals(senderNumber)
                ? secondAccount : firstAccount;

        // Verify ownership again
        AppUser owner = sender.getOwner();

        if (owner == null || !owner.getUsername().equals(senderUsername)) {
            throw new IllegalArgumentException(
                    "You are not authorized to transfer from this account."
            );
        }

        // Check available balance
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient account balance."
            );
        }

        // Update both balances
        sender.setBalance(sender.getBalance().subtract(amount));

        receiver.setBalance(receiver.getBalance().add(amount));

        bankAccountRepository.save(sender);
        bankAccountRepository.save(receiver);

        // Generate a shared transfer reference
        String reference = UUID.randomUUID().toString();

        String description = request.getDescription() == null
                || request.getDescription().isBlank()
                ? "Money transfer"
                : request.getDescription().trim();

        // Create sender transaction
        BankTransaction debit = new BankTransaction();

        debit.setAccount(sender);
        debit.setTransactionType("TRANSFER_SENT");
        debit.setAmount(amount);
        debit.setDescription(
                description + " | To: " + receiver.getAccountNumber()
        );
        debit.setTransferReference(reference);
        debit.setStatus("SUCCESS");

        // Create receiver transaction
        BankTransaction credit = new BankTransaction();

        credit.setAccount(receiver);
        credit.setTransactionType("TRANSFER_RECEIVED");
        credit.setAmount(amount);
        credit.setDescription(
                description + " | From: " + sender.getAccountNumber()
        );
        credit.setTransferReference(reference);
        credit.setStatus("SUCCESS");

        // Save both transaction records
        transactionRepository.save(debit);
        transactionRepository.save(credit);

        return reference;
    }
}


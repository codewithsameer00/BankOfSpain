
package com.bankofspain.bankofspain.service;

import com.bankofspain.bankofspain.dto.RegistrationRequest;

import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.AppUser;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankAccount;

import com.bankofspain.bankofspain.repository.UserRepository;
import com.bankofspain.bankofspain.repository.BankAccountRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom random = new SecureRandom();

    public RegistrationService(
            UserRepository userRepository,
            BankAccountRepository bankAccountRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public BankAccount register(RegistrationRequest request) {

        // Check duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "This username is already taken."
            );
        }

        // Create user
        AppUser user = new AppUser();

        user.setFullName(request.getFullName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setFatherName(request.getFatherName());
        user.setMotherName(request.getMotherName());
        user.setDemoAddress(request.getDemoAddress());
        user.setGender(request.getGender());
        user.setMaritalStatus(request.getMaritalStatus());
        user.setUsername(request.getUsername());

        // Securely hash password
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        // Save user
        AppUser savedUser = userRepository.save(user);

        // Generate unique 12-digit account number
        String accountNumber;

        do {
            long number = 100000000000L
                    + random.nextLong(900000000000L);

            accountNumber = String.valueOf(number);

        } while (
                bankAccountRepository.existsByAccountNumber(accountNumber)
        );

        // Create bank account
        BankAccount account = new BankAccount();

        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        account.setOwner(savedUser);

        // Save account
        return bankAccountRepository.save(account);
    }
}


package com.bankofspain.bankofspain.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "Please enter the recipient account number")
    private String recipientAccountNumber;

    @NotNull(message = "Please enter the transfer amount")
    @DecimalMin(value = "0.01", message = "Minimum transfer amount is 0.01")
    @Digits(integer = 17, fraction = 2,
            message = "Invalid transfer amount")
    private BigDecimal amount;

    @Size(max = 255, message = "Description is too long")
    private String description;

    public TransferRequest() {
    }

    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public void setRecipientAccountNumber(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


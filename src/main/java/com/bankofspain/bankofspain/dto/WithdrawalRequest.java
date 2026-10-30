
package com.bankofspain.bankofspain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class WithdrawalRequest {

    @NotNull(message = "Please enter the withdrawal amount")
    @DecimalMin(
            value = "0.01",
            message = "Minimum withdrawal amount is ₹0.01"
    )
    @Digits(
            integer = 17,
            fraction = 2,
            message = "Please enter a valid amount with up to 2 decimal places"
    )
    private BigDecimal amount;

    @Size(
            max = 255,
            message = "Description cannot exceed 255 characters"
    )
    private String description;

    public WithdrawalRequest() {
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


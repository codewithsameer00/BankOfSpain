
package com.bankofspain.bankofspain.controller;

import com.bankofspain.bankofspain.dto.WithdrawalRequest;
import com.bankofspain.bankofspain.service.WithdrawalService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @GetMapping("/withdraw")
    public String showWithdrawalPage(Model model) {

        model.addAttribute(
                "withdrawalRequest",
                new WithdrawalRequest()
        );

        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String processWithdrawal(
            @Valid @ModelAttribute("withdrawalRequest")
            WithdrawalRequest request,

            BindingResult bindingResult,

            Principal principal,

            Model model,

            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "withdraw";
        }

        try {

            String reference = withdrawalService.withdraw(
                    principal.getName(),
                    request
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Your demo withdrawal was completed successfully."
            );

            redirectAttributes.addFlashAttribute(
                    "transactionReference",
                    reference
            );

            return "redirect:/withdraw";

        } catch (IllegalArgumentException exception) {

            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "withdraw";
        }
    }
}


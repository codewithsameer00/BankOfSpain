
package com.bankofspain.bankofspain.controller;

import com.bankofspain.bankofspain.dto.DepositRequest;
import com.bankofspain.bankofspain.service.DepositService;

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
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping("/deposit")
    public String showDepositPage(Model model) {

        model.addAttribute("depositRequest", new DepositRequest());

        return "deposit";
    }

    @PostMapping("/deposit")
    public String processDeposit(
            @Valid @ModelAttribute("depositRequest") DepositRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "deposit";
        }

        try {

            String reference = depositService.deposit(
                    principal.getName(),
                    request
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Your demo deposit was completed successfully."
            );

            redirectAttributes.addFlashAttribute(
                    "transactionReference",
                    reference
            );

            return "redirect:/deposit";

        } catch (IllegalArgumentException exception) {

            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "deposit";
        }
    }
}


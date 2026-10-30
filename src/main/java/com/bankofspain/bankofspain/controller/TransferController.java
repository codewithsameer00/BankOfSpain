
package com.bankofspain.bankofspain.controller;

import com.bankofspain.bankofspain.dto.TransferRequest;
import com.bankofspain.bankofspain.service.TransferService;

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
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    // Display transfer page
    @GetMapping("/transfer")
    public String showTransferPage(Model model) {

        if (!model.containsAttribute("transferRequest")) {
            model.addAttribute("transferRequest", new TransferRequest());
        }

        return "transfer";
    }

    // Process money transfer
    @PostMapping("/transfer")
    public String processTransfer(
            @Valid @ModelAttribute("transferRequest")
            TransferRequest request,

            BindingResult result,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validate form
        if (result.hasErrors()) {
            return "transfer";
        }

        try {

            // Identify authenticated customer
            String username = principal.getName();

            // Execute money transfer
            String reference = transferService.transfer(
                    username,
                    request
            );

            // Send success information
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Your money transfer was completed successfully!"
            );

            redirectAttributes.addFlashAttribute(
                    "transferReference",
                    reference
            );

            return "redirect:/transfer";

        } catch (IllegalArgumentException exception) {

            result.reject(
                    "transfer.failed",
                    exception.getMessage()
            );

            return "transfer";
        }
    }
}


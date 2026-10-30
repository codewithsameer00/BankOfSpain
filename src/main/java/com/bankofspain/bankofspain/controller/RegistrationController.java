
package com.bankofspain.bankofspain.controller;

import com.bankofspain.bankofspain.dto.RegistrationRequest;
import com.bankofspain.bankofspain.service.RegistrationService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(
            RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // Display login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Display registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        if (!model.containsAttribute("registrationRequest")) {
            model.addAttribute(
                    "registrationRequest",
                    new RegistrationRequest()
            );
        }

        return "register";
    }

    // Process registration form
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registrationRequest")
            RegistrationRequest request,

            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Check validation errors
        if (result.hasErrors()) {
            return "register";
        }

        try {

            // Register user and create bank account
            var account = registrationService.register(request);

            // Send success information to next page
            redirectAttributes.addFlashAttribute(
                    "registrationSuccess",
                    "Your Bank of Spain account has been created successfully!"
            );

            redirectAttributes.addFlashAttribute(
                    "accountNumber",
                    account.getAccountNumber()
            );

            return "redirect:/login";

        } catch (IllegalArgumentException exception) {

            result.rejectValue(
                    "username",
                    "username.duplicate",
                    exception.getMessage()
            );

            return "register";
        }
    }
}


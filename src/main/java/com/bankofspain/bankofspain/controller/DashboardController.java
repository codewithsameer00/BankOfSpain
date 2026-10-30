
package com.bankofspain.bankofspain.controller;

import com.bankofspain.bankofspain.service.DashboardService;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankAccount;
import com.bankofspain.bankofspain.com.bankofspain.bankofspain.entity.BankTransaction;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Principal principal, Model model) {

        String username = principal.getName();

        BankAccount account = dashboardService.getAccount(username);

        List<BankTransaction> transactions =
                dashboardService.getTransactions(username);

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        model.addAttribute("customer", account.getOwner());

        return "dashboard";
    }

    @GetMapping("/transactions")
    public String showTransactions(Principal principal, Model model) {

        String username = principal.getName();

        BankAccount account = dashboardService.getAccount(username);

        List<BankTransaction> transactions =
                dashboardService.getTransactions(username);

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        model.addAttribute("customer", account.getOwner());

        return "transactions";
    }
}

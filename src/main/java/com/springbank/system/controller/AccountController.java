package com.springbank.system.controller;

import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/{id}/balance")
    public Money getBalance(@PathVariable Long id) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found.");
        }
        return accountOpt.get().getBalance();
    }
}
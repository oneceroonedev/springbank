package com.springbank.system.controller;

import com.springbank.system.model.Transaction;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.repository.AccountHolderRepository;
import com.springbank.system.repository.AccountRepository;
import com.springbank.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    // Get all accounts of a user by their ID
    @GetMapping("/{userId}/accounts")
    public List<Account> getAccountsByUser(@PathVariable Long userId) {
        AccountHolder user = accountHolderRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return accountRepository.findAllByOwnerId(userId);
    }

    // Make a transfer
    @PostMapping("/{userId}/transfer")
    public Transaction makeTransfer(
            @PathVariable Long userId,
            @RequestParam Long senderAccountId,
            @RequestParam Long receiverAccountId,
            @RequestBody Transaction transaction
    ) {
        AccountHolder user = accountHolderRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account sender = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Issuing account not found"));

        // Manual validation (without Spring Security)
        if (!sender.getPrimaryOwner().getId().equals(userId) &&
                (sender.getSecondaryOwner() == null || !sender.getSecondaryOwner().getId().equals(userId))) {
            throw new SecurityException("You aren't authorized to make transfers from this account.");
        }

        return transactionService.transfer(senderAccountId, receiverAccountId, transaction);
    }
}
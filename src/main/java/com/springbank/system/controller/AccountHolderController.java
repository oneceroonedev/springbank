package com.springbank.system.controller;

import com.springbank.system.dto.TransferRequestDTO;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.repository.AccountHolderRepository;
import com.springbank.system.repository.AccountRepository;
import com.springbank.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/account-holders")
public class AccountHolderController {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    // Get all AccountHolders
    @GetMapping
    public List<AccountHolder> getAll() {
        return accountHolderRepository.findAll();
    }

    // Create a new AccountHolder
    @PostMapping
    public AccountHolder create(@RequestBody AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    // View balance of your own account
    @GetMapping("/{userId}/accounts/{accountId}/balance")
    public BigDecimal getOwnAccountBalance(
            @PathVariable Long userId,
            @PathVariable Long accountId
    ) {
        AccountHolder holder = accountHolderRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getPrimaryOwner().getId().equals(userId) &&
                (account.getSecondaryOwner() == null || !account.getSecondaryOwner().getId().equals(userId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to this account");
        }

        return account.getBalance().getAmount();
    }

    // Transfer using DTO and service
    @PostMapping("/{accountHolderId}/transfer")
    public String transferMoney(
            @PathVariable Long accountHolderId,
            @RequestBody TransferRequestDTO transferDTO
    ) {
        transactionService.transfer(accountHolderId, transferDTO);
        return "Transfer completed successfully.";
    }

    @GetMapping("/{id}/accounts")
    public List<Account> getAccounts(
            @PathVariable Long id,
            @RequestParam(required = false) Long requesterId
    ) {
        if (requesterId == null || !requesterId.equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        accountHolderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        return accountRepository.findAllByOwnerId(id);
    }
}
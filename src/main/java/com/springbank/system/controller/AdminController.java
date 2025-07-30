package com.springbank.system.controller;

import com.springbank.system.dto.BalanceUpdateDTO;
import com.springbank.system.dto.CheckingAccountDTO;
import com.springbank.system.dto.CreditCardDTO;
import com.springbank.system.dto.SavingsAccountDTO;
import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.accounts.CheckingAccount;
import com.springbank.system.model.accounts.CreditCard;
import com.springbank.system.model.accounts.Savings;
import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.model.users.Admin;
import com.springbank.system.repository.AccountRepository;
import com.springbank.system.repository.AdminRepository;
import com.springbank.system.service.AccountHolderService;
import com.springbank.system.service.CheckingAccountService;
import com.springbank.system.service.CreditCardService;
import com.springbank.system.service.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountHolderService accountHolderService;

    @Autowired
    private CheckingAccountService checkingAccountService;

    @Autowired
    private SavingsService savingsService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AdminRepository adminRepository;

    // AccountHolder
    @PostMapping("/account-holders")
    public AccountHolder createAccountHolder(@RequestBody AccountHolder accountHolder) {
        return accountHolderService.createAccountHolder(accountHolder);
    }

    @GetMapping("/account-holders")
    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderService.getAllAccountHolders();
    }

    // Checking Account con DTO
    @PostMapping("/checking-accounts")
    public ResponseEntity<?> createCheckingAccount(@RequestBody CheckingAccountDTO dto) {
        return new ResponseEntity<>(checkingAccountService.createCheckingAccount(dto), HttpStatus.CREATED);
    }

    @GetMapping("/checking-accounts")
    public List<CheckingAccount> getAllCheckingAccounts() {
        return checkingAccountService.getAllCheckingAccounts();
    }

    // Savings
    @PostMapping("/savings-accounts")
    public Savings createSavingsAccount(@RequestBody SavingsAccountDTO savingsDto) {
        return savingsService.createSavingsAccount(savingsDto);
    }

    @GetMapping("/savings-accounts")
    public List<Savings> getAllSavingsAccounts() {
        return savingsService.getAllSavingsAccounts();
    }

    // Credit Card
    @PostMapping("/credit-cards")
    public ResponseEntity<CreditCard> createCreditCard(@RequestBody CreditCardDTO dto) {
        return ResponseEntity.ok(creditCardService.createFromDTO(dto));
    }

    @GetMapping("/credit-cards")
    public List<CreditCard> getAllCreditCards() {
        return creditCardService.getAllCreditCards();
    }

    // Delete
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        if (!accountRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account ID " + id + " not found.");
        }

        accountRepository.deleteById(id);
        return ResponseEntity.ok("Account ID " + id + " successfully removed.");
    }

    // Admin
    @PostMapping("/create")
    public Admin createAdmin(@RequestBody Admin admin) {
        return adminRepository.save(admin);
    }

    // Update balance
    @PatchMapping("/accounts/{id}/balance")
    public ResponseEntity<String> updateAccountBalance(@PathVariable Long id, @RequestBody BalanceUpdateDTO dto) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        Account account = accountOpt.get();
        account.setBalance(new Money(dto.getNewBalance()));
        accountRepository.save(account);
        return ResponseEntity.ok("Balance updated successfully.");
    }
}
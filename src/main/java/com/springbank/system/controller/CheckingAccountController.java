package com.springbank.system.controller;

import com.springbank.system.dto.CheckingAccountDTO;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.accounts.CheckingAccount;
import com.springbank.system.service.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checking-accounts")
public class CheckingAccountController {

    @Autowired
    private CheckingAccountService checkingAccountService;

    @PostMapping
    public Account create(@RequestBody CheckingAccountDTO dto) {
        return checkingAccountService.createCheckingAccount(dto);
    }

    @GetMapping
    public List<CheckingAccount> getAll() {
        return checkingAccountService.getAllCheckingAccounts();
    }
}
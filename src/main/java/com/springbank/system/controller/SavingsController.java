package com.springbank.system.controller;

import com.springbank.system.dto.SavingsAccountDTO;
import com.springbank.system.model.accounts.Savings;
import com.springbank.system.service.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/savings")
public class SavingsController {

    @Autowired
    private SavingsService savingsService;

    @GetMapping
    public List<Savings> getAll() {
        return savingsService.getAllSavingsAccounts();
    }

    @PostMapping
    public Savings create(@RequestBody SavingsAccountDTO savingsDto) {
        return savingsService.createSavingsAccount(savingsDto);
    }
}
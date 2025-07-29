package com.springbank.system.controller;

import com.springbank.system.model.Transaction;
import com.springbank.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public Transaction transfer(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestBody Transaction transaction) {
        return transactionService.transfer(senderId, receiverId, transaction);
    }
}
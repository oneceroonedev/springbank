package com.springbank.system.controller;

import com.springbank.system.model.accounts.CreditCard;
import com.springbank.system.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping
    public List<CreditCard> getAll() {
        return creditCardService.getAllCreditCards();
    }

    @PostMapping
    public CreditCard create(@RequestBody CreditCard creditCard) {
        return creditCardService.createCreditCard(creditCard);
    }
}
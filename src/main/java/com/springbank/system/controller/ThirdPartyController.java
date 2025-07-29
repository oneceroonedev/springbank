package com.springbank.system.controller;

import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.users.ThirdParty;
import com.springbank.system.repository.AccountRepository;
import com.springbank.system.repository.ThirdPartyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/third-party")
public class ThirdPartyController {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Create a ThirdParty
    @PostMapping
    public ThirdParty createThirdParty(@RequestBody ThirdParty thirdParty) {
        return thirdPartyRepository.save(thirdParty);
    }

    // Send money to an account (third party deposits into system account)
    @PatchMapping("/send-money")
    @Transactional
    public ResponseEntity<String> sendMoney(
            @RequestHeader("hashed-key") String hashedKey,
            @RequestParam Long accountId,
            @RequestParam String secretKey,
            @RequestBody Money amount) {

        ThirdParty thirdParty = validateThirdParty(hashedKey);
        Account account = validateAccountWithSecretKey(accountId, secretKey);

        account.getBalance().increaseAmount(amount.getAmount());
        accountRepository.save(account);

        return ResponseEntity.ok("Money sent successfully. New balance: " + account.getBalance().getAmount());
    }

    // Receive money (withdraw from a system account to a third party)
    @PatchMapping("/receive-money")
    @Transactional
    public ResponseEntity<String> receiveMoney(
            @RequestHeader("hashed-key") String hashedKey,
            @RequestParam Long accountId,
            @RequestParam String secretKey,
            @RequestBody Money amount) {

        ThirdParty thirdParty = validateThirdParty(hashedKey);
        Account account = validateAccountWithSecretKey(accountId, secretKey);

        BigDecimal currentBalance = account.getBalance().getAmount();

        if (currentBalance.compareTo(amount.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        account.getBalance().decreaseAmount(amount.getAmount());
        accountRepository.save(account);

        return ResponseEntity.ok("Money withdrawn successfully. New balance: " + account.getBalance().getAmount());
    }

    // Auxiliary private methods
    private ThirdParty validateThirdParty(String hashedKey) {
        return thirdPartyRepository.findAll()
                .stream()
                .filter(tp -> tp.getHashedKey().equals(hashedKey))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid hashed key"));
    }

    private Account validateAccountWithSecretKey(Long accountId, String secretKey) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getSecretKey().equals(secretKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid secret key for this account");
        }

        return account;
    }
}
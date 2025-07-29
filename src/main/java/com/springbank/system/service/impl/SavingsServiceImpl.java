package com.springbank.system.service.impl;

import com.springbank.system.dto.SavingsAccountDTO;
import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.Savings;
import com.springbank.system.model.enums.Status;
import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.repository.AccountHolderRepository;
import com.springbank.system.repository.SavingsRepository;
import com.springbank.system.service.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SavingsServiceImpl implements SavingsService {

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    private static final BigDecimal PENALTY_FEE = new BigDecimal("40");

    @Override
    public Savings createSavingsAccount(SavingsAccountDTO dto) {
        // Search for owners
        AccountHolder primaryOwner = accountHolderRepository.findById(dto.getPrimaryOwnerId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Primary owner not found"));

        AccountHolder secondaryOwner = null;
        if (dto.getSecondaryOwnerId() != null) {
            secondaryOwner = accountHolderRepository.findById(dto.getSecondaryOwnerId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Secondary owner not found"));
        }

        // Build account
        Savings savings = new Savings();
        savings.setBalance(new Money(dto.getBalance(), "EUR"));
        savings.setPrimaryOwner(primaryOwner);
        savings.setSecondaryOwner(secondaryOwner);
        savings.setSecretKey(dto.getSecretKey());
        savings.setStatus(Status.ACTIVE); // default

        if (dto.getMinimumBalance() != null) {
            savings.setMinimumBalance(new Money(dto.getMinimumBalance(), "EUR"));
        }

        if (dto.getInterestRate() != null) {
            savings.setInterestRate(dto.getInterestRate());
        }

        applyPenaltyIfNeeded(savings);
        applyInterestIfNeeded(savings);

        return savingsRepository.save(savings);
    }

    @Override
    public List<Savings> getAllSavingsAccounts() {
        List<Savings> accounts = savingsRepository.findAll();
        for (Savings acc : accounts) {
            applyInterestIfNeeded(acc);
        }
        return savingsRepository.saveAll(accounts);
    }

    private void applyPenaltyIfNeeded(Savings account) {
        Money balance = account.getBalance();
        BigDecimal amount = balance.getAmount();

        if (amount.compareTo(account.getMinimumBalance().getAmount()) < 0) {
            balance.setAmount(amount.subtract(PENALTY_FEE));
        }
    }

    private void applyInterestIfNeeded(Savings account) {
        LocalDate lastInterest = account.getLastInterestDate();
        LocalDate now = LocalDate.now();

        long yearsPassed = ChronoUnit.YEARS.between(lastInterest, now);

        if (yearsPassed >= 1) {
            BigDecimal balance = account.getBalance().getAmount();
            BigDecimal interestRate = account.getInterestRate();

            for (int i = 0; i < yearsPassed; i++) {
                balance = balance.add(balance.multiply(interestRate));
            }

            account.getBalance().setAmount(balance);
            account.setLastInterestDate(lastInterest.plusYears(yearsPassed));
        }
    }
}
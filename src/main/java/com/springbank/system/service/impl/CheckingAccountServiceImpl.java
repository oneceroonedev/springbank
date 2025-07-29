package com.springbank.system.service.impl;

import com.springbank.system.dto.CheckingAccountDTO;
import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.accounts.CheckingAccount;
import com.springbank.system.model.accounts.StudentChecking;
import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.repository.AccountHolderRepository;
import com.springbank.system.repository.CheckingAccountRepository;
import com.springbank.system.repository.StudentCheckingRepository;
import com.springbank.system.service.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class CheckingAccountServiceImpl implements CheckingAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    private static final BigDecimal PENALTY_FEE = new BigDecimal("40");

    public Account createCheckingAccount(CheckingAccountDTO dto) {
        if (dto.getPrimaryOwnerId() == null) {
            throw new IllegalArgumentException("Primary Owner ID must be provided.");
        }

        AccountHolder primaryOwner = accountHolderRepository.findById(dto.getPrimaryOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Primary owner not found with ID: " + dto.getPrimaryOwnerId()));

        AccountHolder secondaryOwner = null;
        if (dto.getSecondaryOwnerId() != null) {
            secondaryOwner = accountHolderRepository.findById(dto.getSecondaryOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Secondary owner not found with ID: " + dto.getSecondaryOwnerId()));
        }

        int age = Period.between(primaryOwner.getDateOfBirth(), LocalDate.now()).getYears();

        if (age < 24) {
            StudentChecking studentChecking = new StudentChecking(
                    dto.getBalance(),
                    primaryOwner,
                    secondaryOwner,
                    null,
                    null
            );
            studentChecking.setSecretKey(dto.getSecretKey());
            applyPenaltyIfNeeded(studentChecking);
            return studentCheckingRepository.save(studentChecking);
        } else {
            CheckingAccount checkingAccount = new CheckingAccount(
                    dto.getBalance(),
                    primaryOwner,
                    secondaryOwner,
                    null,
                    null
            );
            checkingAccount.setSecretKey(dto.getSecretKey());
            applyPenaltyIfNeeded(checkingAccount);
            return checkingAccountRepository.save(checkingAccount);
        }
    }

    @Override
    public List<CheckingAccount> getAllCheckingAccounts() {
        return checkingAccountRepository.findAll();
    }

    private void applyPenaltyIfNeeded(Account account) {
        Money balance = account.getBalance();
        BigDecimal amount = balance.getAmount();

        if (account instanceof CheckingAccount checkingAccount) {
            if (amount.compareTo(checkingAccount.getMinimumBalance().getAmount()) < 0) {
                balance.setAmount(amount.subtract(PENALTY_FEE));
            }
        }
    }
}
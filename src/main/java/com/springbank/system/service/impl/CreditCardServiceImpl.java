package com.springbank.system.service.impl;

import com.springbank.system.dto.CreditCardDTO;
import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.CreditCard;
import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.repository.AccountHolderRepository;
import com.springbank.system.repository.CreditCardRepository;
import com.springbank.system.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Override
    public CreditCard createCreditCard(CreditCard creditCard) {
        applyInterestIfNeeded(creditCard);
        return creditCardRepository.save(creditCard);
    }

    public CreditCard createFromDTO(CreditCardDTO dto) {
        AccountHolder primaryOwner = accountHolderRepository.findById(dto.getPrimaryOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Primary owner not found"));

        AccountHolder secondaryOwner = null;
        if (dto.getSecondaryOwnerId() != null) {
            secondaryOwner = accountHolderRepository.findById(dto.getSecondaryOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Secondary owner not found"));
        }

        CreditCard creditCard = new CreditCard();
        creditCard.setBalance(new Money(dto.getBalance()));
        creditCard.setCreditLimit(new Money(dto.getCreditLimit()));
        creditCard.setInterestRate(dto.getInterestRate());
        creditCard.setPrimaryOwner(primaryOwner);
        creditCard.setSecondaryOwner(secondaryOwner);
        creditCard.setSecretKey(dto.getSecretKey());


        return createCreditCard(creditCard); // Calls the original method to apply interest
    }

    @Override
    public List<CreditCard> getAllCreditCards() {
        List<CreditCard> accounts = creditCardRepository.findAll();
        for (CreditCard acc : accounts) {
            applyInterestIfNeeded(acc);
        }
        return creditCardRepository.saveAll(accounts);
    }

    private void applyInterestIfNeeded(CreditCard account) {
        LocalDate lastInterest = account.getLastInterestDate();
        LocalDate now = LocalDate.now();

        long monthsPassed = ChronoUnit.MONTHS.between(lastInterest, now);

        if (monthsPassed >= 1) {
            BigDecimal balance = account.getBalance().getAmount();
            BigDecimal monthlyInterestRate = account.getInterestRate()
                    .divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP);

            for (int i = 0; i < monthsPassed; i++) {
                balance = balance.add(balance.multiply(monthlyInterestRate));
            }

            account.getBalance().setAmount(balance);
            account.setLastInterestDate(lastInterest.plusMonths(monthsPassed));
        }
    }
}
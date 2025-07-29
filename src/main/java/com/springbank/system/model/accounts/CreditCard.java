package com.springbank.system.model.accounts;

import com.springbank.system.model.Money;
import com.springbank.system.model.enums.Status;
import com.springbank.system.model.users.AccountHolder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account {

    private static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal("100");
    private static final BigDecimal MAX_CREDIT_LIMIT = new BigDecimal("100000");

    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.2");
    private static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");

    @Embedded
    private Money creditLimit;

    private BigDecimal interestRate;


    @Setter
    private LocalDate lastInterestDate;

    public CreditCard() {
        super();
        this.creditLimit = new Money(DEFAULT_CREDIT_LIMIT);
        this.interestRate = DEFAULT_INTEREST_RATE;
        this.lastInterestDate = LocalDate.now();
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                      LocalDate creationDate, Status status, BigDecimal interestRate, Money creditLimit) {
        super(balance, primaryOwner, secondaryOwner, creationDate, status);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
        this.lastInterestDate = LocalDate.now();
    }

    public void setCreditLimit(Money creditLimit) {
        if (creditLimit == null || creditLimit.getAmount().compareTo(MAX_CREDIT_LIMIT) > 0) {
            this.creditLimit = new Money(MAX_CREDIT_LIMIT);
        } else {
            this.creditLimit = creditLimit;
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null || interestRate.compareTo(MIN_INTEREST_RATE) < 0) {
            this.interestRate = MIN_INTEREST_RATE;
        } else {
            this.interestRate = interestRate;
        }
    }
}
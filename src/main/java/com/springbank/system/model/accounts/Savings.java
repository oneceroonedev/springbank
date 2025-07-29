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
public class Savings extends Account {

    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");
    private static final BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");
    private static final BigDecimal DEFAULT_MINIMUM_BALANCE = new BigDecimal("1000");
    private static final BigDecimal MIN_MINIMUM_BALANCE = new BigDecimal("100");

    @Embedded
    private Money minimumBalance;

    private BigDecimal interestRate;

    @Setter
    private LocalDate lastInterestDate;

    public Savings() {
        super();
        this.minimumBalance = new Money(DEFAULT_MINIMUM_BALANCE);
        this.interestRate = DEFAULT_INTEREST_RATE;
        this.lastInterestDate = LocalDate.now();
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   LocalDate creationDate, Status status, BigDecimal interestRate, Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner, creationDate, status);
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
        this.lastInterestDate = LocalDate.now();
    }

    public void setMinimumBalance(Money minimumBalance) {
        if (minimumBalance == null || minimumBalance.getAmount().compareTo(MIN_MINIMUM_BALANCE) < 0) {
            this.minimumBalance = new Money(MIN_MINIMUM_BALANCE);
        } else {
            this.minimumBalance = minimumBalance;
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null || interestRate.compareTo(MAX_INTEREST_RATE) > 0) {
            this.interestRate = MAX_INTEREST_RATE;
        } else {
            this.interestRate = interestRate;
        }
    }
}
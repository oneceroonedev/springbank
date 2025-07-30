package com.springbank.system.model.accounts;

import com.springbank.system.model.Money;
import com.springbank.system.model.enums.Status;
import com.springbank.system.model.users.AccountHolder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CheckingAccount extends Account {

    private static final BigDecimal DEFAULT_MINIMUM_BALANCE_AMOUNT = new BigDecimal("250");
    private static final BigDecimal DEFAULT_MONTHLY_MAINTENANCE_FEE_AMOUNT = new BigDecimal("12");

    // Getters and Setters
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance = new Money(DEFAULT_MINIMUM_BALANCE_AMOUNT);

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency"))
    })
    private Money monthlyMaintenanceFee = new Money(DEFAULT_MONTHLY_MAINTENANCE_FEE_AMOUNT);

    // Constructors
    public CheckingAccount() {}

    public CheckingAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, LocalDate creationDate, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate, status);
    }
}
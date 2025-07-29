package com.springbank.system.model.accounts;

import com.springbank.system.model.enums.Status;
import com.springbank.system.model.Money;
import com.springbank.system.model.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class StudentChecking extends Account {

    public StudentChecking() {
        super();
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                           LocalDate creationDate, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate, status);
    }
}
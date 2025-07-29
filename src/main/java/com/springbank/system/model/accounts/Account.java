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
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Embedded
    private Money balance;

    @Setter
    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    private AccountHolder primaryOwner;

    @Setter
    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;

    @Setter
    @Column(nullable = false)
    private String secretKey; // Added for authentication of third-party operations

    private LocalDate creationDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    private final BigDecimal penaltyFee = new BigDecimal("40");

    // Constructors

    public Account() {
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
    }

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   LocalDate creationDate, Status status) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creationDate = creationDate != null ? creationDate : LocalDate.now();
        this.status = status != null ? status : Status.ACTIVE;
    }
}
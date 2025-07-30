package com.springbank.system.config;

import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.*;
import com.springbank.system.model.enums.Status;
import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.model.users.Admin;
import com.springbank.system.model.users.ThirdParty;
import com.springbank.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public void run(String... args) throws Exception {
        // Account Holders
        AccountHolder john = new AccountHolder("John Doe", LocalDate.of(1990, 1, 15), null, null);
        AccountHolder alice = new AccountHolder("Alice Smith", LocalDate.of(2005, 4, 10), null, null);
        AccountHolder bob = new AccountHolder("Bob Johnson", LocalDate.of(1985, 6, 25), null, null);

        accountHolderRepository.save(john);
        accountHolderRepository.save(alice);
        accountHolderRepository.save(bob);

        // Admin
        Admin admin = new Admin("adminUser");
        adminRepository.save(admin);

        // Third Party
        ThirdParty thirdParty = new ThirdParty("Stripe", "hashed-key-123");
        thirdPartyRepository.save(thirdParty);

        // Checking Account (> 24 años)
        CheckingAccount checking = new CheckingAccount(
                new Money(new BigDecimal("1500")),
                john,
                null,
                LocalDate.now(),
                Status.ACTIVE
        );
        checking.setSecretKey("johnSecret");
        checkingAccountRepository.save(checking);

        // Student Checking (< 24 años)
        StudentChecking student = new StudentChecking(
                new Money(new BigDecimal("300")),
                alice,
                null,
                LocalDate.now(),
                Status.ACTIVE
        );
        student.setSecretKey("aliceSecret");
        studentCheckingRepository.save(student);

        // Savings account
        Savings savings = new Savings(
                new Money(new BigDecimal("5000")),
                bob,
                null,
                LocalDate.now(),
                Status.ACTIVE,
                new BigDecimal("0.0025"), // Interest
                new Money(new BigDecimal("1000")) // Minimum
        );
        savings.setSecretKey("bobSecret");
        savingsRepository.save(savings);

        // Credit card account
        CreditCard creditCard = new CreditCard(
                new Money(new BigDecimal("2000")),
                bob,
                null,
                LocalDate.now(),
                Status.ACTIVE,
                new BigDecimal("0.2"), // Interest
                new Money(new BigDecimal("100")) // Credit limit
        );
        creditCard.setSecretKey("bobCreditSecret");
        creditCardRepository.save(creditCard);

        System.out.println("✅ Sample data inserted into the database");
    }
}
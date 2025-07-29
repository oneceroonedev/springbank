package com.springbank.system;

import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.CheckingAccount;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void saveAccount_PersistsSuccessfully() {
        CheckingAccount account = new CheckingAccount();
        account.setBalance(new Money(new BigDecimal("500")));
        account.setSecretKey("secret123");

        Account saved = accountRepository.save(account);

        assertNotNull(saved.getId());
        assertEquals("secret123", saved.getSecretKey());
    }
}
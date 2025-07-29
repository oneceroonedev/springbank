package com.springbank.system;

import com.springbank.system.model.Money;
import com.springbank.system.model.Transaction;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.accounts.CheckingAccount;
import com.springbank.system.repository.AccountRepository;
import com.springbank.system.repository.TransactionRepository;
import com.springbank.system.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    private AccountRepository accountRepository = mock(AccountRepository.class);
    private TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private TransactionServiceImpl service;

    @BeforeEach
    void setup() {
        service = new TransactionServiceImpl();
        ReflectionTestUtils.setField(service, "accountRepository", accountRepository);
        ReflectionTestUtils.setField(service, "transactionRepository", transactionRepository);
    }

    @Test
    void transfer_ShouldApplyPenaltyFee_WhenBalanceDropsBelowMinimum() {
        CheckingAccount sender = new CheckingAccount();
        sender.setId(1L);
        sender.setBalance(new Money(new BigDecimal("260")));
        sender.setMinimumBalance(new Money(new BigDecimal("250")));
        sender.setSecretKey("senderSecret");

        Account receiver = new CheckingAccount();
        receiver.setId(2L);
        receiver.setBalance(new Money(new BigDecimal("500")));

        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("20"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Transaction result = service.transfer(1L, 2L, transaction);

        assertEquals(new BigDecimal("200"), sender.getBalance().getAmount());
    }
}

package com.springbank.system;

import com.springbank.system.model.Money;
import com.springbank.system.model.accounts.CreditCard;
import com.springbank.system.repository.CreditCardRepository;
import com.springbank.system.service.impl.CreditCardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreditCardServiceImplTest {

    private CreditCardRepository creditCardRepository = mock(CreditCardRepository.class);
    private CreditCardServiceImpl creditCardService;

    @BeforeEach
    void setup() {
        creditCardService = new CreditCardServiceImpl();
        ReflectionTestUtils.setField(creditCardService, "creditCardRepository", creditCardRepository);
    }

    @Test
    void applyInterestIfNeeded_ShouldAddInterest_WhenOneMonthPassed() {
        CreditCard card = new CreditCard();
        card.setInterestRate(new BigDecimal("0.12")); // 12% per year = 1% per month
        card.setLastInterestDate(LocalDate.now().minusMonths(1));
        card.setBalance(new Money(new BigDecimal("1000")));

        when(creditCardRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CreditCard updated = creditCardService.createCreditCard(card);

        BigDecimal expected = new BigDecimal("1010.00"); // 1% of 1000
        assertEquals(0, expected.compareTo(updated.getBalance().getAmount()));
    }
}

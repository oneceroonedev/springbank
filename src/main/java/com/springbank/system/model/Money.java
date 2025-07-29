package com.springbank.system.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class Money {
    private BigDecimal amount;
    private String currency;

    public Money() {}

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
        this.currency = "EUR";
    }

    public void increaseAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    public void decreaseAmount(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
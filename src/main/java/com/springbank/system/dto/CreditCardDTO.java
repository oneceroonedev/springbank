package com.springbank.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreditCardDTO {
    private BigDecimal balance;
    private Long primaryOwnerId;
    private Long secondaryOwnerId; // optional
    private BigDecimal creditLimit;
    private BigDecimal interestRate;
    private String secretKey;
}
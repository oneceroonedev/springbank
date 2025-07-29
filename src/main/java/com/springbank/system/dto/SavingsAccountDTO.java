package com.springbank.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SavingsAccountDTO {
    private BigDecimal balance;
    private Long primaryOwnerId;
    private Long secondaryOwnerId; // optional
    private String secretKey;
    private BigDecimal minimumBalance;
    private BigDecimal interestRate;
}
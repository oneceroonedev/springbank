package com.springbank.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class BalanceUpdateDTO {
    private BigDecimal newBalance;
}
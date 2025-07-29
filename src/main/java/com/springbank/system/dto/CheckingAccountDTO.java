package com.springbank.system.dto;

import com.springbank.system.model.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckingAccountDTO {
    private Money balance;
    private String secretKey;
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
}
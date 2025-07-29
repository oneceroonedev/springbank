package com.springbank.system.service;

import com.springbank.system.dto.CreditCardDTO;
import com.springbank.system.model.accounts.CreditCard;
import java.util.List;

public interface CreditCardService {
    CreditCard createCreditCard(CreditCard creditCard);
    CreditCard createFromDTO(CreditCardDTO dto);
    List<CreditCard> getAllCreditCards();
}
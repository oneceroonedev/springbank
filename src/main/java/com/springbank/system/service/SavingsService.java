package com.springbank.system.service;

import com.springbank.system.dto.SavingsAccountDTO;
import com.springbank.system.model.accounts.Savings;

import java.util.List;

public interface SavingsService {
    Savings createSavingsAccount(SavingsAccountDTO dto);
    List<Savings> getAllSavingsAccounts();
}

package com.springbank.system.service;

import com.springbank.system.dto.CheckingAccountDTO;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.accounts.CheckingAccount;

import java.util.List;

public interface CheckingAccountService {
    Account createCheckingAccount(CheckingAccountDTO dto);
    List<CheckingAccount> getAllCheckingAccounts();
}
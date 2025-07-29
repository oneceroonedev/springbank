package com.springbank.system.service;

import com.springbank.system.model.users.AccountHolder;

import java.util.List;

public interface AccountHolderService {
    AccountHolder createAccountHolder(AccountHolder accountHolder);
    List<AccountHolder> getAllAccountHolders();
    AccountHolder getAccountHolderById(Long id);
}
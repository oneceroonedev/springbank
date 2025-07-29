package com.springbank.system.service.impl;

import com.springbank.system.model.users.AccountHolder;
import com.springbank.system.repository.AccountHolderRepository;
import com.springbank.system.service.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Override
    public AccountHolder createAccountHolder(AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    @Override
    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.findAll();
    }

    @Override
    public AccountHolder getAccountHolderById(Long id) {
        Optional<AccountHolder> optional = accountHolderRepository.findById(id);
        return optional.orElseThrow(() -> new IllegalArgumentException("AccountHolder not found with ID: " + id));
    }
}
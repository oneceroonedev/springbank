package com.springbank.system.service;

import com.springbank.system.dto.TransferRequestDTO;
import com.springbank.system.model.Transaction;

public interface TransactionService {
    Transaction transfer(Long senderAccountId, Long receiverAccountId, Transaction transaction);
    void transfer(Long accountHolderId, TransferRequestDTO transferDTO);
}
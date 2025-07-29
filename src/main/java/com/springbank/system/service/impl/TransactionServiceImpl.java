package com.springbank.system.service.impl;

import com.springbank.system.dto.TransferRequestDTO;
import com.springbank.system.model.Transaction;
import com.springbank.system.model.accounts.*;
import com.springbank.system.repository.AccountRepository;
import com.springbank.system.repository.TransactionRepository;
import com.springbank.system.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private static final BigDecimal PENALTY_FEE = new BigDecimal("40");

    @Override
    @Transactional
    public Transaction transfer(Long senderAccountId, Long receiverAccountId, Transaction transaction) {
        Optional<Account> senderOpt = accountRepository.findById(senderAccountId);
        Optional<Account> receiverOpt = accountRepository.findById(receiverAccountId);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            throw new IllegalArgumentException("One or both accounts don't exist.");
        }

        Account sender = senderOpt.get();
        Account receiver = receiverOpt.get();

        BigDecimal amount = transaction.getAmount();

        if (sender.getBalance().getAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        // Discount the transmitter
        sender.getBalance().decreaseAmount(amount);

        // Apply penalty if applicable
        applyPenaltyIfBelowMinimum(sender);

        // Accredit the receiver
        receiver.getBalance().increaseAmount(amount);

        // Persist changes
        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Save the transaction
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void transfer(Long accountHolderId, TransferRequestDTO transferDTO) {
        Long senderAccountId = transferDTO.getFromAccountId();
        Long receiverAccountId = transferDTO.getToAccountId();
        String recipientName = transferDTO.getRecipientName();
        BigDecimal amount = transferDTO.getAmount();

        Account sender = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found."));

        Account receiver = accountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found."));

        // Verify ownership
        boolean isOwner = sender.getPrimaryOwner().getId().equals(accountHolderId) ||
                (sender.getSecondaryOwner() != null && sender.getSecondaryOwner().getId().equals(accountHolderId));

        if (!isOwner) {
            throw new IllegalArgumentException("You aren't the owner of the source account.");
        }

        // Verify receiver name
        boolean recipientMatches = receiver.getPrimaryOwner().getName().equalsIgnoreCase(recipientName) ||
                (receiver.getSecondaryOwner() != null && receiver.getSecondaryOwner().getName().equalsIgnoreCase(recipientName));

        if (!recipientMatches) {
            throw new IllegalArgumentException("The receiver's name doesn't match.");
        }

        // Verify funds
        if (sender.getBalance().getAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        // Make a transfer
        sender.getBalance().decreaseAmount(amount);
        applyPenaltyIfBelowMinimum(sender);
        receiver.getBalance().increaseAmount(amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    private void applyPenaltyIfBelowMinimum(Account account) {
        if (account instanceof CheckingAccount checking) {
            if (checking.getBalance().getAmount()
                    .compareTo(checking.getMinimumBalance().getAmount()) < 0) {
                checking.getBalance().decreaseAmount(PENALTY_FEE);
            }
        } else if (account instanceof Savings savings) {
            if (savings.getBalance().getAmount()
                    .compareTo(savings.getMinimumBalance().getAmount()) < 0) {
                savings.getBalance().decreaseAmount(PENALTY_FEE);
            }
        }
    }
}
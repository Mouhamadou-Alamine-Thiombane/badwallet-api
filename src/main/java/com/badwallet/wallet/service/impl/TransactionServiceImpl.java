package com.badwallet.wallet.service.impl;

import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import com.badwallet.wallet.repository.TransactionRepository;
import com.badwallet.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByWallet(Wallet wallet) {
        return transactionRepository.findByWalletOrderByTimestampDesc(wallet);
    }
}

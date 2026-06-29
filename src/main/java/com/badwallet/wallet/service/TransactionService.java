package com.badwallet.wallet.service;

import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;

import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    List<Transaction> findByWallet(Wallet wallet);
}

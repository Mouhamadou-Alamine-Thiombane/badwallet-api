package com.badwallet.wallet.service;

import com.badwallet.wallet.dto.*;
import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet createWallet(WalletRequestDTO request);

    Page<Wallet> listWallets(Pageable pageable);

    Wallet getWalletByPhone(String phoneNumber);

    BigDecimal getBalance(String phoneNumber);

    Transaction deposit(Long walletId, DepositRequestDTO request);

    Transaction processDeposit(Wallet wallet, DepositRequestDTO request);

    Transaction withdraw(WithdrawRequestDTO request);

    Transaction processWithdrawal(Wallet wallet, BigDecimal amount);

    Transaction transfer(TransferRequestDTO request);

    Transaction processTransfer(Wallet sender, Wallet receiver, BigDecimal amount);

    Transaction pay(PaymentRequestDTO request);

    List<Transaction> payFactures(PaymentRequestDTO request);

    List<Transaction> getTransactionHistory(String phoneNumber);

    void seedWallets(int numWallets, int eventsPerWallet);
}

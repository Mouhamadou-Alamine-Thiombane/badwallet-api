package com.badwallet.wallet.command;

import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import com.badwallet.wallet.service.WalletService;

import java.math.BigDecimal;

public class WithdrawCommand implements WalletCommand {

    private final Wallet wallet;
    private final BigDecimal amount;
    private final WalletService walletService;

    public WithdrawCommand(Wallet wallet, BigDecimal amount, WalletService walletService) {
        this.wallet = wallet;
        this.amount = amount;
        this.walletService = walletService;
    }

    @Override
    public Transaction execute() {
        return walletService.processWithdrawal(wallet, amount);
    }
}

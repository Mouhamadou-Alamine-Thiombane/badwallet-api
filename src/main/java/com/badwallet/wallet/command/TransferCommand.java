package com.badwallet.wallet.command;

import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import com.badwallet.wallet.service.WalletService;

import java.math.BigDecimal;

public class TransferCommand implements WalletCommand {

    private final Wallet sender;
    private final Wallet receiver;
    private final BigDecimal amount;
    private final WalletService walletService;

    public TransferCommand(Wallet sender, Wallet receiver, BigDecimal amount, WalletService walletService) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.walletService = walletService;
    }

    @Override
    public Transaction execute() {
        return walletService.processTransfer(sender, receiver, amount);
    }
}

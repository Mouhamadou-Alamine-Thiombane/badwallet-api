package com.badwallet.wallet.command;

import com.badwallet.wallet.dto.DepositRequestDTO;
import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import com.badwallet.wallet.service.WalletService;

public class DepositCommand implements WalletCommand {

    private final Wallet wallet;
    private final DepositRequestDTO request;
    private final WalletService walletService;

    public DepositCommand(Wallet wallet, DepositRequestDTO request, WalletService walletService) {
        this.wallet = wallet;
        this.request = request;
        this.walletService = walletService;
    }

    @Override
    public Transaction execute() {
        return walletService.processDeposit(wallet, request);
    }
}

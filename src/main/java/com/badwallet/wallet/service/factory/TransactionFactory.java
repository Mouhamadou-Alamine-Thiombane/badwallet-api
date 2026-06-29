package com.badwallet.wallet.service.factory;

import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.TransactionStatus;
import com.badwallet.wallet.entity.TransactionType;
import com.badwallet.wallet.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * FACTORY PATTERN : centralise la création des différents types
 * de Transaction (dépôt, retrait, transfert, paiement).
 */
@Component
public class TransactionFactory {

    public Transaction createDepositTransaction(Wallet wallet, BigDecimal amount, String paymentMethod) {
        return Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .status(TransactionStatus.COMPLETED)
                .build();
    }

    public Transaction createWithdrawTransaction(Wallet wallet, BigDecimal amount, BigDecimal fees) {
        return Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount.negate())
                .fees(fees)
                .status(TransactionStatus.COMPLETED)
                .build();
    }

    public Transaction createTransferTransaction(Wallet sender, Wallet receiver, BigDecimal amount, BigDecimal fees) {
        return Transaction.builder()
                .wallet(sender)
                .type(TransactionType.TRANSFER)
                .amount(amount.negate())
                .fees(fees)
                .receiverPhone(receiver.getPhoneNumber())
                .status(TransactionStatus.COMPLETED)
                .build();
    }

    public Transaction createPaymentTransaction(Wallet wallet, BigDecimal amount, String serviceName) {
        return Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.PAYMENT)
                .amount(amount.negate())
                .serviceName(serviceName)
                .status(TransactionStatus.COMPLETED)
                .build();
    }
}

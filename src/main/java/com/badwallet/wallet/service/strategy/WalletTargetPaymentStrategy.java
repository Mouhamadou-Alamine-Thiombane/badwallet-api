package com.badwallet.wallet.service.strategy;

import com.badwallet.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Qualifier("walletTarget")
public class WalletTargetPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(Wallet wallet, BigDecimal amount) {
        // Simulation d'un dépôt provenant d'un autre portefeuille
        wallet.setBalance(wallet.getBalance().add(amount));
        return new PaymentResult(true, "Dépôt depuis un autre portefeuille effectué");
    }
}

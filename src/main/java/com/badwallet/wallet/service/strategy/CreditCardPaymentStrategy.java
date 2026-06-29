package com.badwallet.wallet.service.strategy;

import com.badwallet.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Qualifier("creditCard")
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(Wallet wallet, BigDecimal amount) {
        // Simulation d'un appel à un gateway de carte bancaire
        wallet.setBalance(wallet.getBalance().add(amount));
        return new PaymentResult(true, "Paiement par carte bancaire accepté");
    }
}

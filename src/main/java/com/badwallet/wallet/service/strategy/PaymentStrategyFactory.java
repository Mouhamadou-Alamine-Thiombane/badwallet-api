package com.badwallet.wallet.service.strategy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Sélectionne dynamiquement la bonne stratégie de paiement
 * en fonction du champ "paymentMethod" reçu dans la requête.
 */
@Component
public class PaymentStrategyFactory {

    private final PaymentStrategy creditCardStrategy;
    private final PaymentStrategy walletTargetStrategy;

    public PaymentStrategyFactory(@Qualifier("creditCard") PaymentStrategy creditCardStrategy,
                                   @Qualifier("walletTarget") PaymentStrategy walletTargetStrategy) {
        this.creditCardStrategy = creditCardStrategy;
        this.walletTargetStrategy = walletTargetStrategy;
    }

    public PaymentStrategy getStrategy(String paymentMethod) {
        return switch (paymentMethod.toUpperCase()) {
            case "CREDIT_CARD" -> creditCardStrategy;
            case "WALLET_TARGET" -> walletTargetStrategy;
            default -> throw new IllegalArgumentException("Méthode de paiement inconnue : " + paymentMethod);
        };
    }
}

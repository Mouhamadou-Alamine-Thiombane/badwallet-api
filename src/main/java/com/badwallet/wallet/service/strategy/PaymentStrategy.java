package com.badwallet.wallet.service.strategy;

import com.badwallet.wallet.entity.Wallet;

import java.math.BigDecimal;

/**
 * STRATEGY PATTERN : chaque méthode de paiement (CREDIT_CARD, WALLET_TARGET...)
 * implémente sa propre logique de traitement du dépôt.
 */
public interface PaymentStrategy {
    PaymentResult processPayment(Wallet wallet, BigDecimal amount);
}

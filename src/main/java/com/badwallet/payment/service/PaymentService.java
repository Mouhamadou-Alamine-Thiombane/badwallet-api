package com.badwallet.payment.service;

import com.badwallet.wallet.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    boolean payService(Wallet wallet, String serviceName, BigDecimal amount);
    boolean payFactureReferences(Wallet wallet, List<String> factureReferences);
}

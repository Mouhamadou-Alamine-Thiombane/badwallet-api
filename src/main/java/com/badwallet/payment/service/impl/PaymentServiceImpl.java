package com.badwallet.payment.service.impl;

import com.badwallet.payment.client.PaymentServiceClient;
import com.badwallet.payment.service.PaymentService;
import com.badwallet.wallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentServiceClient paymentServiceClient;

    @Override
    public boolean payService(Wallet wallet, String serviceName, BigDecimal amount) {
        return paymentServiceClient.payFacture(wallet.getCode(), serviceName, amount.doubleValue());
    }

    @Override
    public boolean payFactureReferences(Wallet wallet, List<String> factureReferences) {
        return paymentServiceClient.payFactureReferences(wallet.getCode(), factureReferences);
    }
}

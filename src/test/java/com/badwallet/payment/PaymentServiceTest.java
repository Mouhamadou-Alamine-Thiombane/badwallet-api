package com.badwallet.payment;

import com.badwallet.payment.client.PaymentServiceClient;
import com.badwallet.payment.service.PaymentService;
import com.badwallet.payment.service.impl.PaymentServiceImpl;
import com.badwallet.wallet.entity.Wallet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Test
    void payService_shouldDelegateToProxyClient() {
        PaymentServiceClient client = mock(PaymentServiceClient.class);
        when(client.payFacture("WLT-0000003", "ISM", 5000.0)).thenReturn(true);

        PaymentService paymentService = new PaymentServiceImpl(client);
        Wallet wallet = Wallet.builder().code("WLT-0000003").build();

        boolean result = paymentService.payService(wallet, "ISM", new BigDecimal("5000"));

        assertTrue(result);
        verify(client, times(1)).payFacture("WLT-0000003", "ISM", 5000.0);
    }
}

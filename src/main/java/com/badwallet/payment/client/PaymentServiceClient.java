package com.badwallet.payment.client;

import com.badwallet.payment.entity.Facture;

import java.time.LocalDate;
import java.util.List;

/**
 * PROXY PATTERN : interface représentant le service externe payment-service.
 * Le badwallet-api ne connaît que ce contrat ; l'implémentation réelle
 * (PaymentServiceClientImpl) se charge de l'appel HTTP distant.
 */
public interface PaymentServiceClient {

    List<Facture> getCurrentMonthInvoices(String walletCode);

    List<Facture> getCurrentMonthInvoicesByUnit(String walletCode, String unite);

    List<Facture> getInvoicesByPeriod(String walletCode, LocalDate debut, LocalDate fin);

    boolean payFacture(String walletCode, String serviceName, double amount);

    boolean payFactureReferences(String walletCode, List<String> factureReferences);
}

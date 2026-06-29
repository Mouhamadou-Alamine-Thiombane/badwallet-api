package com.paymentservice.service;

import com.paymentservice.entity.Facture;

import java.time.LocalDate;
import java.util.List;

public interface FactureService {
    List<Facture> getCurrentMonthInvoices(String walletCode);
    List<Facture> getCurrentMonthInvoicesByUnit(String walletCode, String unite);
    List<Facture> getInvoicesByPeriod(String walletCode, LocalDate debut, LocalDate fin);
    boolean payByService(String walletCode, String serviceName, double amount);
    boolean payByReferences(String walletCode, List<String> references);
}

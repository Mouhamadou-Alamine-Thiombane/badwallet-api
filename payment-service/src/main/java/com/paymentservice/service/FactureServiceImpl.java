package com.paymentservice.service;

import com.paymentservice.entity.Facture;
import com.paymentservice.repository.FactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;

    @Override
    public List<Facture> getCurrentMonthInvoices(String walletCode) {
        return factureRepository.findByWalletCodeAndPayeeFalse(walletCode);
    }

    @Override
    public List<Facture> getCurrentMonthInvoicesByUnit(String walletCode, String unite) {
        return factureRepository.findByWalletCodeAndServiceAndPayeeFalse(walletCode, unite);
    }

    @Override
    public List<Facture> getInvoicesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
        return factureRepository.findByWalletCodeAndDateEcheanceBetween(walletCode, debut, fin);
    }

    @Override
    public boolean payByService(String walletCode, String serviceName, double amount) {
        List<Facture> factures = factureRepository.findByWalletCodeAndServiceAndPayeeFalse(walletCode, serviceName);
        factures.forEach(f -> f.setPayee(true));
        factureRepository.saveAll(factures);
        return true;
    }

    @Override
    public boolean payByReferences(String walletCode, List<String> references) {
        List<Facture> factures = factureRepository.findByReferenceIn(references);
        factures.forEach(f -> f.setPayee(true));
        factureRepository.saveAll(factures);
        return true;
    }
}

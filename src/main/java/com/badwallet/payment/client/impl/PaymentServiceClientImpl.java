package com.badwallet.payment.client.impl;

import com.badwallet.payment.client.PaymentServiceClient;
import com.badwallet.payment.entity.Facture;
import com.badwallet.shared.exceptions.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implémentation concrète du Proxy : effectue les appels REST réels
 * vers le micro-service payment-service (port 8081).
 */
@Slf4j
@Component
public class PaymentServiceClientImpl implements PaymentServiceClient {

    private final RestTemplate restTemplate;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    public PaymentServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Facture> getCurrentMonthInvoices(String walletCode) {
        String url = paymentServiceUrl + "/api/factures/" + walletCode + "/current";
        return fetch(url);
    }

    @Override
    public List<Facture> getCurrentMonthInvoicesByUnit(String walletCode, String unite) {
        String url = paymentServiceUrl + "/api/factures/" + walletCode + "/current?unite=" + unite;
        return fetch(url);
    }

    @Override
    public List<Facture> getInvoicesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
        String url = paymentServiceUrl + "/api/factures/" + walletCode + "/periode?debut=" + debut + "&fin=" + fin;
        return fetch(url);
    }

    @Override
    public boolean payFacture(String walletCode, String serviceName, double amount) {
        String url = paymentServiceUrl + "/api/factures/" + walletCode + "/pay?service=" + serviceName + "&amount=" + amount;
        try {
            restTemplate.postForObject(url, null, Void.class);
            return true;
        } catch (RestClientException e) {
            log.error("Erreur lors du paiement via payment-service : {}", e.getMessage());
            throw new ExternalServiceException("Erreur lors de l'appel au service de paiement");
        }
    }

    @Override
    public boolean payFactureReferences(String walletCode, List<String> factureReferences) {
        String refs = String.join(",", factureReferences);
        String url = paymentServiceUrl + "/api/factures/" + walletCode + "/pay-references?refs=" + refs;
        try {
            restTemplate.postForObject(url, null, Void.class);
            return true;
        } catch (RestClientException e) {
            log.error("Erreur lors du paiement de factures spécifiques : {}", e.getMessage());
            throw new ExternalServiceException("Erreur lors de l'appel au service de paiement");
        }
    }

    private List<Facture> fetch(String url) {
        try {
            Facture[] factures = restTemplate.getForObject(url, Facture[].class);
            return factures != null ? Arrays.asList(factures) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("Erreur lors de l'appel au payment-service ({}) : {}", url, e.getMessage());
            throw new ExternalServiceException("Erreur lors de l'appel au service de paiement externe");
        }
    }
}

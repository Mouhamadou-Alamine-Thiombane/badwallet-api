package com.badwallet.payment.adapter;

import com.badwallet.payment.client.PaymentServiceClient;
import com.badwallet.payment.dto.FactureResponseDTO;
import com.badwallet.payment.entity.Facture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ADAPTER PATTERN : convertit les objets Facture (format du service externe)
 * vers les DTO exposés par notre API, en isolant le reste de l'application
 * des spécificités de payment-service.
 */
@Component
@RequiredArgsConstructor
public class PaymentServiceAdapter {

    private final PaymentServiceClient paymentServiceClient;

    public List<FactureResponseDTO> getCurrentInvoices(String walletCode) {
        return paymentServiceClient.getCurrentMonthInvoices(walletCode)
                .stream().map(this::adaptToDTO).collect(Collectors.toList());
    }

    public List<FactureResponseDTO> getCurrentInvoicesByUnit(String walletCode, String unite) {
        return paymentServiceClient.getCurrentMonthInvoicesByUnit(walletCode, unite)
                .stream().map(this::adaptToDTO).collect(Collectors.toList());
    }

    public List<FactureResponseDTO> getInvoicesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
        return paymentServiceClient.getInvoicesByPeriod(walletCode, debut, fin)
                .stream().map(this::adaptToDTO).collect(Collectors.toList());
    }

    private FactureResponseDTO adaptToDTO(Facture facture) {
        return FactureResponseDTO.builder()
                .reference(facture.getReference())
                .serviceName(facture.getService())
                .amount(facture.getMontant())
                .dueDate(facture.getDateEcheance())
                .status(mapStatus(facture))
                .build();
    }

    private String mapStatus(Facture facture) {
        return facture.isPayee() ? "PAYÉE" : "IMPAYÉE";
    }
}

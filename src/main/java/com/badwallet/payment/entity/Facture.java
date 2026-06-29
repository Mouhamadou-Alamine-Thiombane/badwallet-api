package com.badwallet.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Représentation locale d'une facture, telle que renvoyée par
 * le micro-service externe payment-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {
    private String reference;     // ex: FAC-ISM-3-1
    private String walletCode;
    private String service;       // ISM, WOYAFAL...
    private BigDecimal montant;
    private LocalDate dateEcheance;
    private boolean payee;
}

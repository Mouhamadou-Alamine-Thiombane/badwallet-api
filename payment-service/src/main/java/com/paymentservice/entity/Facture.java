package com.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference; // FAC-ISM-3-1

    @Column(nullable = false)
    private String walletCode; // WLT-0000003

    @Column(nullable = false)
    private String service; // ISM, WOYAFAL

    @Column(nullable = false)
    private BigDecimal montant;

    private LocalDate dateEcheance;

    @Builder.Default
    private boolean payee = false;
}

package com.paymentservice.config;

import com.paymentservice.entity.Facture;
import com.paymentservice.repository.FactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Initialise quelques factures de test (ISM, WOYAFAL) pour la démo. */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final FactureRepository factureRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        for (int i = 1; i <= 5; i++) {
            factureRepository.save(Facture.builder()
                    .reference("FAC-ISM-3-" + i)
                    .walletCode("WLT-0000003")
                    .service("ISM")
                    .montant(BigDecimal.valueOf(2500 * i))
                    .dateEcheance(LocalDate.now().plusDays(i))
                    .payee(false)
                    .build());

            factureRepository.save(Facture.builder()
                    .reference("FAC-WOYAFAL-3-" + i)
                    .walletCode("WLT-0000003")
                    .service("WOYAFAL")
                    .montant(BigDecimal.valueOf(1500 * i))
                    .dateEcheance(LocalDate.now().plusDays(i))
                    .payee(false)
                    .build());
        }
    }
}

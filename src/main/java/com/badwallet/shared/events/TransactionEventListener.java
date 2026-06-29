package com.badwallet.shared.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Observer concret : réagit aux TransactionEvent de façon asynchrone
 * (simulateur de notification / log d'audit).
 */
@Slf4j
@Component
public class TransactionEventListener {

    @Async
    @EventListener
    public void handleTransactionEvent(TransactionEvent event) {
        var tx = event.getTransaction();
        log.info("[NOTIFICATION] Transaction #{} de type {} pour le montant {} effectuée sur le wallet {}",
                tx.getId(), tx.getType(), tx.getAmount(),
                tx.getWallet() != null ? tx.getWallet().getPhoneNumber() : "N/A");
    }
}

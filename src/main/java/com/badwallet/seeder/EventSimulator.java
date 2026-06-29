package com.badwallet.seeder;

import com.badwallet.shared.events.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Simule des traitements additionnels (audit, métriques...) déclenchés
 * par les événements de transaction publiés via l'Observer Pattern.
 */
@Slf4j
@Component
public class EventSimulator {

    @Async
    @EventListener
    public void simulate(TransactionEvent event) {
        log.debug("[AUDIT] Simulation de traitement additionnel pour la transaction #{}",
                event.getTransaction().getId());
    }
}

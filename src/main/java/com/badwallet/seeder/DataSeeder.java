package com.badwallet.seeder;

import com.badwallet.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Initialise quelques portefeuilles de démonstration au démarrage
 * de l'application (en plus de l'endpoint POST /api/wallets/seed).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final WalletService walletService;

    @EventListener(ApplicationReadyEvent.class)
    public void seedOnStartup() {
        log.info("Démarrage du seeding initial de démonstration...");
        walletService.seedWallets(3, 5);
    }
}

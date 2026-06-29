package com.badwallet.wallet.command;

import com.badwallet.wallet.entity.Transaction;
import org.springframework.stereotype.Component;

/** Invoker du Command Pattern : déclenche l'exécution de la commande reçue. */
@Component
public class WalletCommandInvoker {

    public Transaction executeCommand(WalletCommand command) {
        return command.execute();
    }
}

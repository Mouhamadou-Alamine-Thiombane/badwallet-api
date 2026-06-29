package com.badwallet.wallet.command;

import com.badwallet.wallet.entity.Transaction;

/** COMMAND PATTERN : encapsule une opération wallet en objet exécutable. */
public interface WalletCommand {
    Transaction execute();
}

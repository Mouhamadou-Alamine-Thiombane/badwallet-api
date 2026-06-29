package com.badwallet.shared.events;

import com.badwallet.wallet.entity.Transaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Événement publié à chaque transaction réussie.
 * Pièce centrale de l'OBSERVER PATTERN : un ou plusieurs listeners
 * réagissent de façon asynchrone (notification, log, audit...).
 */
@Getter
public class TransactionEvent extends ApplicationEvent {

    private final Transaction transaction;

    public TransactionEvent(Transaction transaction) {
        super(transaction);
        this.transaction = transaction;
    }
}

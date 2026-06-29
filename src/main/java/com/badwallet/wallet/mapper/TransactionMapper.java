package com.badwallet.wallet.mapper;

import com.badwallet.wallet.dto.TransactionResponseDTO;
import com.badwallet.wallet.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDTO toDTO(Transaction tx) {
        if (tx == null) return null;
        return TransactionResponseDTO.builder()
                .id(tx.getId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .fees(tx.getFees())
                .paymentMethod(tx.getPaymentMethod())
                .receiverPhone(tx.getReceiverPhone())
                .serviceName(tx.getServiceName())
                .status(tx.getStatus())
                .timestamp(tx.getTimestamp())
                .build();
    }
}

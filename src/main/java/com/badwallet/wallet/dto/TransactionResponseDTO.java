package com.badwallet.wallet.dto;

import com.badwallet.wallet.entity.TransactionStatus;
import com.badwallet.wallet.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal fees;
    private String paymentMethod;
    private String receiverPhone;
    private String serviceName;
    private TransactionStatus status;
    private LocalDateTime timestamp;
}

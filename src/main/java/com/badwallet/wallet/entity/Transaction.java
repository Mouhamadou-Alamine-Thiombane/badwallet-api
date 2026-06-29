package com.badwallet.wallet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    @Builder.Default
    private BigDecimal fees = BigDecimal.ZERO;

    private String paymentMethod;

    private String receiverPhone;

    private String serviceName; // ISM, WOYAFAL, etc.

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}

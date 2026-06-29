package com.badwallet.wallet.dto;

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
public class WalletResponseDTO {
    private Long id;
    private String code;
    private String phoneNumber;
    private String email;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
}

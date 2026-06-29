package com.badwallet.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositRequestDTO {

    @NotNull
    @Positive(message = "Le montant doit être positif")
    private BigDecimal amount;

    @NotBlank(message = "La méthode de paiement est obligatoire (CREDIT_CARD, WALLET_TARGET)")
    private String paymentMethod;
}

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
public class WalletRequestDTO {

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;

    private String email;

    @NotNull(message = "Le solde initial est obligatoire")
    @Positive(message = "Le solde initial doit être positif")
    private BigDecimal initialBalance;

    private String code;

    private String currency;
}

package com.badwallet.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String serviceName; // ISM, WOYAFAL...

    @Positive(message = "Le montant doit être positif")
    private BigDecimal amount;

    // Utilisé uniquement pour /pay-factures
    private List<String> factureReferences;
}

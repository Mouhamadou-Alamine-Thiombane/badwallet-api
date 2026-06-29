package com.badwallet.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactureResponseDTO {
    private String reference;
    private String serviceName;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String status; // PAYÉE / IMPAYÉE
}

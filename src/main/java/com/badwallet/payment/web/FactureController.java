package com.badwallet.payment.web;

import com.badwallet.payment.adapter.PaymentServiceAdapter;
import com.badwallet.payment.dto.FactureResponseDTO;
import com.badwallet.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur "Proxy API" exposant les factures du service externe
 * payment-service au travers de badwallet-api.
 */
@RestController
@RequestMapping("/api/external/factures")
@RequiredArgsConstructor
public class FactureController {

    private final PaymentServiceAdapter paymentServiceAdapter;

    @GetMapping("/{walletCode}/current")
    public ApiResponse<List<FactureResponseDTO>> getCurrentInvoices(
            @PathVariable String walletCode,
            @RequestParam(required = false) String unite) {
        List<FactureResponseDTO> factures = (unite == null || unite.isBlank())
                ? paymentServiceAdapter.getCurrentInvoices(walletCode)
                : paymentServiceAdapter.getCurrentInvoicesByUnit(walletCode, unite);
        return ApiResponse.success(factures, "Factures du mois en cours récupérées");
    }

    @GetMapping("/{walletCode}/periode")
    public ApiResponse<List<FactureResponseDTO>> getInvoicesByPeriod(
            @PathVariable String walletCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<FactureResponseDTO> factures = paymentServiceAdapter.getInvoicesByPeriod(walletCode, debut, fin);
        return ApiResponse.success(factures, "Factures sur la période récupérées");
    }
}

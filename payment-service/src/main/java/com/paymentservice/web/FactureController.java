package com.paymentservice.web;

import com.paymentservice.entity.Facture;
import com.paymentservice.service.FactureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;

    @GetMapping("/{walletCode}/current")
    public List<Facture> getCurrent(@PathVariable String walletCode,
                                     @RequestParam(required = false) String unite) {
        return (unite == null || unite.isBlank())
                ? factureService.getCurrentMonthInvoices(walletCode)
                : factureService.getCurrentMonthInvoicesByUnit(walletCode, unite);
    }

    @GetMapping("/{walletCode}/periode")
    public List<Facture> getByPeriod(@PathVariable String walletCode,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return factureService.getInvoicesByPeriod(walletCode, debut, fin);
    }

    @PostMapping("/{walletCode}/pay")
    public boolean pay(@PathVariable String walletCode,
                        @RequestParam String service,
                        @RequestParam double amount) {
        return factureService.payByService(walletCode, service, amount);
    }

    @PostMapping("/{walletCode}/pay-references")
    public boolean payByReferences(@PathVariable String walletCode,
                                    @RequestParam String refs) {
        List<String> references = List.of(refs.split(","));
        return factureService.payByReferences(walletCode, references);
    }
}

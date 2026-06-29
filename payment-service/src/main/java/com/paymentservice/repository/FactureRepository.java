package com.paymentservice.repository;

import com.paymentservice.entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findByWalletCodeAndPayeeFalse(String walletCode);
    List<Facture> findByWalletCodeAndServiceAndPayeeFalse(String walletCode, String service);
    List<Facture> findByWalletCodeAndDateEcheanceBetween(String walletCode, LocalDate debut, LocalDate fin);
    Optional<Facture> findByReference(String reference);
    List<Facture> findByReferenceIn(List<String> references);
}

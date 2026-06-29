package com.badwallet.wallet.web;

import com.badwallet.shared.response.ApiResponse;
import com.badwallet.wallet.dto.*;
import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import com.badwallet.wallet.mapper.TransactionMapper;
import com.badwallet.wallet.mapper.WalletMapper;
import com.badwallet.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final TransactionMapper transactionMapper;

    /** 1.1 — Seeder asynchrone */
    @PostMapping("/seed")
    public ApiResponse<String> seed(@RequestParam(defaultValue = "10") int numWallets,
                                     @RequestParam(defaultValue = "100") int eventsPerWallet) {
        walletService.seedWallets(numWallets, eventsPerWallet);
        return ApiResponse.success(
                "Seeding démarré en arrière-plan pour " + numWallets + " portefeuilles",
                "Seeding lancé (asynchrone)");
    }

    /** 1.2 — Créer un portefeuille */
    @PostMapping
    public ApiResponse<WalletResponseDTO> createWallet(@Valid @RequestBody WalletRequestDTO request) {
        Wallet wallet = walletService.createWallet(request);
        return ApiResponse.success(walletMapper.toDTO(wallet), "Portefeuille créé avec succès");
    }

    /** 1.3 — Lister tous les portefeuilles (paginé) */
    @GetMapping
    public ApiResponse<List<WalletResponseDTO>> listWallets(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Wallet> wallets = walletService.listWallets(pageable);
        List<WalletResponseDTO> dtos = wallets.getContent().stream().map(walletMapper::toDTO).collect(Collectors.toList());
        return ApiResponse.success(dtos, "Page " + page + " sur " + wallets.getTotalPages());
    }

    /** 1.4 — Consulter un portefeuille par téléphone */
    @GetMapping("/{phoneNumber}")
    public ApiResponse<WalletResponseDTO> getWallet(@PathVariable String phoneNumber) {
        Wallet wallet = walletService.getWalletByPhone(phoneNumber);
        return ApiResponse.success(walletMapper.toDTO(wallet));
    }

    /** 1.5 — Consulter uniquement le solde */
    @GetMapping("/{phoneNumber}/balance")
    public ApiResponse<BigDecimal> getBalance(@PathVariable String phoneNumber) {
        return ApiResponse.success(walletService.getBalance(phoneNumber), "Solde récupéré");
    }

    /** 1.6 — Dépôt (CREDIT_CARD, WALLET_TARGET) */
    @PostMapping("/{id}/deposit")
    public ApiResponse<TransactionResponseDTO> deposit(@PathVariable Long id, @Valid @RequestBody DepositRequestDTO request) {
        Transaction transaction = walletService.deposit(id, request);
        return ApiResponse.success(transactionMapper.toDTO(transaction), "Dépôt effectué avec succès");
    }

    /** 1.7 — Retrait (frais 1% plafonnés à 5000 CFA) */
    @PostMapping("/withdraw")
    public ApiResponse<TransactionResponseDTO> withdraw(@Valid @RequestBody WithdrawRequestDTO request) {
        Transaction transaction = walletService.withdraw(request);
        return ApiResponse.success(transactionMapper.toDTO(transaction), "Retrait effectué avec succès");
    }

    /** 1.8 — Transfert entre deux portefeuilles */
    @PostMapping("/transfer")
    public ApiResponse<TransactionResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO request) {
        Transaction transaction = walletService.transfer(request);
        return ApiResponse.success(transactionMapper.toDTO(transaction), "Transfert effectué avec succès");
    }

    /** 1.9 — Payer une facture du mois en cours (via payment-service) */
    @PostMapping("/pay")
    public ApiResponse<TransactionResponseDTO> pay(@Valid @RequestBody PaymentRequestDTO request) {
        Transaction transaction = walletService.pay(request);
        return ApiResponse.success(transactionMapper.toDTO(transaction), "Paiement effectué avec succès");
    }

    /** 1.10 — Payer des factures spécifiques (par référence) */
    @PostMapping("/pay-factures")
    public ApiResponse<List<TransactionResponseDTO>> payFactures(@Valid @RequestBody PaymentRequestDTO request) {
        List<Transaction> transactions = walletService.payFactures(request);
        List<TransactionResponseDTO> dtos = transactions.stream().map(transactionMapper::toDTO).collect(Collectors.toList());
        return ApiResponse.success(dtos, "Factures spécifiques payées avec succès");
    }

    /** 1.11 — Historique des transactions */
    @GetMapping("/{phoneNumber}/transactions")
    public ApiResponse<List<TransactionResponseDTO>> getHistory(@PathVariable String phoneNumber) {
        List<TransactionResponseDTO> dtos = walletService.getTransactionHistory(phoneNumber)
                .stream().map(transactionMapper::toDTO).collect(Collectors.toList());
        return ApiResponse.success(dtos, "Historique récupéré");
    }
}

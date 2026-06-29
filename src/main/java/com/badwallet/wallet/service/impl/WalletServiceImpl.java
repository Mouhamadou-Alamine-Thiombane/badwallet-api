package com.badwallet.wallet.service.impl;

import com.badwallet.payment.service.PaymentService;
import com.badwallet.shared.events.TransactionEvent;
import com.badwallet.shared.exceptions.InsufficientBalanceException;
import com.badwallet.shared.exceptions.WalletNotFoundException;
import com.badwallet.shared.utils.Constants;
import com.badwallet.wallet.command.DepositCommand;
import com.badwallet.wallet.command.TransferCommand;
import com.badwallet.wallet.command.WalletCommandInvoker;
import com.badwallet.wallet.command.WithdrawCommand;
import com.badwallet.wallet.dto.*;
import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import com.badwallet.wallet.repository.WalletRepository;
import com.badwallet.wallet.service.TransactionService;
import com.badwallet.wallet.service.WalletService;
import com.badwallet.wallet.service.factory.TransactionFactory;
import com.badwallet.wallet.service.fees.TransferFeeCalculator;
import com.badwallet.wallet.service.fees.WithdrawalFeeCalculator;
import com.badwallet.wallet.service.strategy.PaymentResult;
import com.badwallet.wallet.service.strategy.PaymentStrategy;
import com.badwallet.wallet.service.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;
    private final TransactionFactory transactionFactory;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final WithdrawalFeeCalculator withdrawalFeeCalculator;
    private final TransferFeeCalculator transferFeeCalculator;
    private final WalletCommandInvoker commandInvoker;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    // ---------------------------------------------------------------
    // CRÉATION / CONSULTATION
    // ---------------------------------------------------------------

    @Override
    @Transactional
    public Wallet createWallet(WalletRequestDTO request) {
        if (walletRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Un portefeuille existe déjà pour ce numéro");
        }
        Wallet wallet = Wallet.builder()
                .code(request.getCode() != null ? request.getCode() : "WLT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .balance(request.getInitialBalance())
                .currency(request.getCurrency() != null ? request.getCurrency() : Constants.DEFAULT_CURRENCY)
                .build();
        return walletRepository.save(wallet);
    }

    @Override
    public Page<Wallet> listWallets(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    @Override
    public Wallet getWalletByPhone(String phoneNumber) {
        return walletRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new WalletNotFoundException("Aucun portefeuille trouvé pour le numéro " + phoneNumber));
    }

    @Override
    public BigDecimal getBalance(String phoneNumber) {
        return getWalletByPhone(phoneNumber).getBalance();
    }

    // ---------------------------------------------------------------
    // DÉPÔT (Strategy + Factory + Command + Observer)
    // ---------------------------------------------------------------

    @Override
    @Transactional
    public Transaction deposit(Long walletId, DepositRequestDTO request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Portefeuille introuvable : id=" + walletId));
        DepositCommand command = new DepositCommand(wallet, request, this);
        return commandInvoker.executeCommand(command);
    }

    @Override
    @Transactional
    public Transaction processDeposit(Wallet wallet, DepositRequestDTO request) {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(request.getPaymentMethod());
        PaymentResult result = strategy.processPayment(wallet, request.getAmount());

        walletRepository.save(wallet);
        Transaction transaction = transactionFactory.createDepositTransaction(wallet, request.getAmount(), request.getPaymentMethod());
        transaction = transactionService.save(transaction);

        eventPublisher.publishEvent(new TransactionEvent(transaction));
        return transaction;
    }

    // ---------------------------------------------------------------
    // RETRAIT (Template Method + Factory + Command + Observer)
    // ---------------------------------------------------------------

    @Override
    @Transactional
    public Transaction withdraw(WithdrawRequestDTO request) {
        Wallet wallet = getWalletByPhone(request.getPhoneNumber());
        WithdrawCommand command = new WithdrawCommand(wallet, request.getAmount(), this);
        return commandInvoker.executeCommand(command);
    }

    @Override
    @Transactional
    public Transaction processWithdrawal(Wallet wallet, BigDecimal amount) {
        BigDecimal fees = withdrawalFeeCalculator.calculateFee(amount);
        BigDecimal total = amount.add(fees);

        if (wallet.getBalance().compareTo(total) < 0) {
            throw new InsufficientBalanceException("Solde insuffisant pour effectuer ce retrait (frais inclus)");
        }

        wallet.setBalance(wallet.getBalance().subtract(total));
        walletRepository.save(wallet);

        Transaction transaction = transactionFactory.createWithdrawTransaction(wallet, amount, fees);
        transaction = transactionService.save(transaction);

        eventPublisher.publishEvent(new TransactionEvent(transaction));
        return transaction;
    }

    // ---------------------------------------------------------------
    // TRANSFERT (Template Method + Factory + Command + Observer)
    // ---------------------------------------------------------------

    @Override
    @Transactional
    public Transaction transfer(TransferRequestDTO request) {
        Wallet sender = getWalletByPhone(request.getSenderPhone());
        Wallet receiver = getWalletByPhone(request.getReceiverPhone());
        TransferCommand command = new TransferCommand(sender, receiver, request.getAmount(), this);
        return commandInvoker.executeCommand(command);
    }

    @Override
    @Transactional
    public Transaction processTransfer(Wallet sender, Wallet receiver, BigDecimal amount) {
        BigDecimal fees = transferFeeCalculator.calculateFee(amount);
        BigDecimal total = amount.add(fees);

        if (sender.getBalance().compareTo(total) < 0) {
            throw new InsufficientBalanceException("Solde insuffisant pour effectuer ce transfert (frais inclus)");
        }

        sender.setBalance(sender.getBalance().subtract(total));
        receiver.setBalance(receiver.getBalance().add(amount));
        walletRepository.save(sender);
        walletRepository.save(receiver);

        Transaction transaction = transactionFactory.createTransferTransaction(sender, receiver, amount, fees);
        transaction = transactionService.save(transaction);

        eventPublisher.publishEvent(new TransactionEvent(transaction));
        return transaction;
    }

    // ---------------------------------------------------------------
    // PAIEMENT DE FACTURES (Proxy + Adapter + Factory + Observer)
    // ---------------------------------------------------------------

    @Override
    @Transactional
    public Transaction pay(PaymentRequestDTO request) {
        Wallet wallet = getWalletByPhone(request.getPhoneNumber());

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Solde insuffisant pour payer cette facture");
        }

        boolean success = paymentService.payService(wallet, request.getServiceName(), request.getAmount());
        if (!success) {
            throw new IllegalStateException("Le paiement a échoué auprès du service externe");
        }

        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        Transaction transaction = transactionFactory.createPaymentTransaction(wallet, request.getAmount(), request.getServiceName());
        transaction = transactionService.save(transaction);

        eventPublisher.publishEvent(new TransactionEvent(transaction));
        return List.of(transaction).get(0);
    }

    @Override
    @Transactional
    public List<Transaction> payFactures(PaymentRequestDTO request) {
        Wallet wallet = getWalletByPhone(request.getPhoneNumber());

        boolean success = paymentService.payFactureReferences(wallet, request.getFactureReferences());
        if (!success) {
            throw new IllegalStateException("Le paiement des factures spécifiques a échoué");
        }

        Transaction transaction = transactionFactory.createPaymentTransaction(
                wallet,
                request.getAmount() != null ? request.getAmount() : BigDecimal.ZERO,
                request.getServiceName());
        transaction = transactionService.save(transaction);
        eventPublisher.publishEvent(new TransactionEvent(transaction));

        return List.of(transaction);
    }

    // ---------------------------------------------------------------
    // HISTORIQUE
    // ---------------------------------------------------------------

    @Override
    public List<Transaction> getTransactionHistory(String phoneNumber) {
        Wallet wallet = getWalletByPhone(phoneNumber);
        return transactionService.findByWallet(wallet);
    }

    // ---------------------------------------------------------------
    // SEEDER ASYNCHRONE
    // ---------------------------------------------------------------

    @Override
    @Async("seederExecutor")
    public void seedWallets(int numWallets, int eventsPerWallet) {
        for (int i = 0; i < numWallets; i++) {
            String phone = "+22177" + String.format("%07d", ThreadLocalRandom.current().nextInt(1000000, 9999999));
            Wallet wallet = Wallet.builder()
                    .code("WLT-SEED-" + i)
                    .phoneNumber(phone)
                    .email("seed" + i + "@badwallet.test")
                    .balance(BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000)))
                    .currency(Constants.DEFAULT_CURRENCY)
                    .build();
            wallet = walletRepository.save(wallet);

            for (int j = 0; j < eventsPerWallet; j++) {
                BigDecimal amount = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(500, 5000));
                Transaction tx = transactionFactory.createDepositTransaction(wallet, amount, "CREDIT_CARD");
                transactionService.save(tx);
            }
        }
    }
}

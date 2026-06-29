package com.badwallet.wallet;

import com.badwallet.payment.service.PaymentService;
import com.badwallet.shared.exceptions.InsufficientBalanceException;
import com.badwallet.wallet.command.WalletCommandInvoker;
import com.badwallet.wallet.dto.WithdrawRequestDTO;
import com.badwallet.wallet.entity.Transaction;
import com.badwallet.wallet.entity.Wallet;
import com.badwallet.wallet.repository.WalletRepository;
import com.badwallet.wallet.service.TransactionService;
import com.badwallet.wallet.service.WalletService;
import com.badwallet.wallet.service.factory.TransactionFactory;
import com.badwallet.wallet.service.fees.TransferFeeCalculator;
import com.badwallet.wallet.service.fees.WithdrawalFeeCalculator;
import com.badwallet.wallet.service.impl.WalletServiceImpl;
import com.badwallet.wallet.service.strategy.PaymentStrategyFactory;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Test
    void withdraw_shouldApplyFeeCappedAt5000() {
        WalletRepository walletRepository = mock(WalletRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        TransactionFactory transactionFactory = new TransactionFactory();
        PaymentStrategyFactory paymentStrategyFactory = mock(PaymentStrategyFactory.class);
        WithdrawalFeeCalculator withdrawalFeeCalculator = new WithdrawalFeeCalculator();
        TransferFeeCalculator transferFeeCalculator = new TransferFeeCalculator();
        WalletCommandInvoker commandInvoker = new WalletCommandInvoker();
        PaymentService paymentService = mock(PaymentService.class);
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

        WalletService walletService = new WalletServiceImpl(
                walletRepository, transactionService, transactionFactory, paymentStrategyFactory,
                withdrawalFeeCalculator, transferFeeCalculator, commandInvoker, paymentService, eventPublisher);

        Wallet wallet = Wallet.builder().id(1L).phoneNumber("+221770000001")
                .balance(new BigDecimal("1000000")).currency("XOF").build();

        when(walletRepository.findByPhoneNumber("+221770000001")).thenReturn(Optional.of(wallet));
        when(transactionService.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        // Montant élevé : les frais (1%) dépasseraient 5000, donc plafonnés à 5000
        Transaction tx = walletService.withdraw(
                WithdrawRequestDTO.builder().phoneNumber("+221770000001").amount(new BigDecimal("1000000")).build());

        assertEquals(new BigDecimal("5000"), tx.getFees());
    }

    @Test
    void withdraw_shouldThrowWhenInsufficientBalance() {
        WalletRepository walletRepository = mock(WalletRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        TransactionFactory transactionFactory = new TransactionFactory();
        PaymentStrategyFactory paymentStrategyFactory = mock(PaymentStrategyFactory.class);
        WithdrawalFeeCalculator withdrawalFeeCalculator = new WithdrawalFeeCalculator();
        TransferFeeCalculator transferFeeCalculator = new TransferFeeCalculator();
        WalletCommandInvoker commandInvoker = new WalletCommandInvoker();
        PaymentService paymentService = mock(PaymentService.class);
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

        WalletService walletService = new WalletServiceImpl(
                walletRepository, transactionService, transactionFactory, paymentStrategyFactory,
                withdrawalFeeCalculator, transferFeeCalculator, commandInvoker, paymentService, eventPublisher);

        Wallet wallet = Wallet.builder().id(1L).phoneNumber("+221770000002")
                .balance(new BigDecimal("100")).currency("XOF").build();

        when(walletRepository.findByPhoneNumber("+221770000002")).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(
                WithdrawRequestDTO.builder().phoneNumber("+221770000002").amount(new BigDecimal("10000")).build()));
    }
}

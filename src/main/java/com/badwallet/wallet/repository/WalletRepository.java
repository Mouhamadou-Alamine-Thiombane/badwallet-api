package com.badwallet.wallet.repository;

import com.badwallet.wallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByPhoneNumber(String phoneNumber);
    Optional<Wallet> findByCode(String code);
    Page<Wallet> findAll(Pageable pageable);
    boolean existsByPhoneNumber(String phoneNumber);
}

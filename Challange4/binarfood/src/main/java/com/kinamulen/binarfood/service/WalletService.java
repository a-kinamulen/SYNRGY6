package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.wallet.request.WalletWebRequest;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.repository.MerchantDetailRepository;
import com.kinamulen.binarfood.repository.UserDetailRepository;
import com.kinamulen.binarfood.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private MerchantDetailRepository merchantDetailRepository;

    public WalletWebResponse topUp(UUID id, WalletWebRequest request) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            wallet.get().setBalance(wallet.get().getBalance() + request.getAmount());
            Wallet updatedWallet = walletRepository.save(wallet.get());
            log.info("Top up success to wallet {} amount {}", id, request.getAmount());
            return toWebResponse(updatedWallet);
        } else {
            log.error("Top up failed, wallet id not found. id: {}", id);
            return null;
        }
    }

    private WalletWebResponse toWebResponse(Wallet wallet) {
        return WalletWebResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .type(wallet.getType())
                .isDeleted(wallet.isDeleted())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .deletedAt(wallet.getDeletedAt())
                .build();
    }
}

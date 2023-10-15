package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.wallet.request.WalletWebRequest;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.repository.MerchantDetailRepository;
import com.kinamulen.binarfood.repository.UserDetailRepository;
import com.kinamulen.binarfood.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
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
            return toWebResponse(updatedWallet);
        } else return null;
    }

    private WalletWebResponse toWebResponse(Wallet updatedWallet) {
        return WalletWebResponse.builder()
                .id(updatedWallet.getId())
                .balance(updatedWallet.getBalance())
                .type(updatedWallet.getType())
                .build();
    }
}

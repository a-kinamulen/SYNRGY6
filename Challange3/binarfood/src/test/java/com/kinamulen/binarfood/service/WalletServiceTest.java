package com.kinamulen.binarfood.service;

import com.kinamulen.binarfood.dto.wallet.request.WalletWebRequest;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.repository.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class WalletServiceTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void topUp_fail(){
        WalletWebRequest request = WalletWebRequest.builder()
                .amount(100.0)
                .build();
        WalletWebResponse response = walletService.topUp(UUID.randomUUID(), request);
        Assertions.assertNull(response);
    }

    @Test
    void topUp_success(){
        WalletWebRequest request = WalletWebRequest.builder()
                .amount(100.0)
                .build();
        List<Wallet> wallets = walletRepository.findAll();
        WalletWebResponse response = walletService.topUp(wallets.get(0).getId(), request);
        Assertions.assertNotNull(response);
    }
}
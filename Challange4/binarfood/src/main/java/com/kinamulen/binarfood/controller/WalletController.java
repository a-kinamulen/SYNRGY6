package com.kinamulen.binarfood.controller;

import com.kinamulen.binarfood.dto.wallet.request.WalletWebRequest;
import com.kinamulen.binarfood.dto.wallet.response.WalletWebResponse;
import com.kinamulen.binarfood.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallet")
@Slf4j
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/{id}")
    public ResponseEntity<WalletWebResponse> topUp(@PathVariable UUID id, @RequestBody WalletWebRequest request) {
        log.info("starting top up for wallet id {}, amount {}", id, request.getAmount());
        WalletWebResponse response =walletService.topUp(id, request);
        return ResponseEntity.ok(response);
    }
}

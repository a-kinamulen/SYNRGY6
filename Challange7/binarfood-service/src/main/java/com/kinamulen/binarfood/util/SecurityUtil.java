package com.kinamulen.binarfood.util;

import com.kinamulen.binarfood.entity.Order;
import com.kinamulen.binarfood.entity.Product;
import com.kinamulen.binarfood.entity.User;
import com.kinamulen.binarfood.entity.Wallet;
import com.kinamulen.binarfood.enums.UserType;
import com.kinamulen.binarfood.repository.OrderRepository;
import com.kinamulen.binarfood.repository.ProductRepository;
import com.kinamulen.binarfood.repository.UserRepository;
import com.kinamulen.binarfood.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class SecurityUtil {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ProductRepository productRepository;

    public Boolean authorizeId(UUID id, String idFromToken) {
        boolean authorized = id.equals(UUID.fromString(idFromToken));
        log.info("User/Merchant is authorized: {}", authorized);
        return authorized;
    }

    public Boolean authorizeOrderToUserId(UUID orderIdFromPath, String userIdFromToken) {
        Optional<Order> order = orderRepository.findById(orderIdFromPath);
        if (order.isEmpty()) {
            log.info("Order to User is authorized: {}", false);
            return false;
        }
        UUID userId = order.get().getUser().getId();
        boolean authorized = userId.equals(UUID.fromString(userIdFromToken));
        log.info("Order to User is authorized: {}", authorized);
        return authorized;
    }

    public Boolean authorizeProductToMerchantId(UUID productIdFromPath, String merchantIdFromToken) {
        Optional<Product> product = productRepository.findById(productIdFromPath);
        if (product.isEmpty()) {
            log.info("Product to Merchant is authorized: {}", false);
            return false;
        }
        UUID merchantId = product.get().getMerchant().getId();
        boolean authorized = merchantId.equals(UUID.fromString(merchantIdFromToken));
        log.info("Product to Merchant is authorized: {}", authorized);
        return authorized;
    }

    public Boolean authorizeWalletToBinarfoodId(UUID walletIdFromPath, String userIdFromToken, String merchantIdFromToken) {
        Optional<Wallet> wallet = walletRepository.findById(walletIdFromPath);
        if (wallet.isEmpty()) {
            log.info("Wallet to User/Merchant is authorized: {}", false);
            return false;
        }
        boolean authorized = false;
        if (wallet.get().getType().equals(UserType.USER)) {
            authorized = wallet.get().getUserDetail().getUser().getId().equals(UUID.fromString(userIdFromToken));
            log.info("Wallet type:user, is authorized: {}", authorized);
            return authorized;
        }
        if (wallet.get().getType().equals(UserType.MERCHANT)) {
            authorized = wallet.get().getMerchantDetail().getMerchant().getId().equals(UUID.fromString(merchantIdFromToken));
            log.info("Wallet type:merchant, is authorized: {}", authorized);
            return authorized;
        }
        return authorized;
    }

    public String generateOtp() {
        Random random = new Random();
        Integer otp = 100000 + random.nextInt(900000);
        return otp.toString();
    }
}

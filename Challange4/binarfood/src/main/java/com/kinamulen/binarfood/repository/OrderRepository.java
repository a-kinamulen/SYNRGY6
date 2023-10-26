package com.kinamulen.binarfood.repository;

import com.kinamulen.binarfood.entity.Order;
import com.kinamulen.binarfood.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Procedure("deduct_user_wallet")
    void deductUserWallet(@Param("orderId") UUID orderId,
                          @Param("username") String username,
                          @Param("Password") String password);

    @Procedure("top_up_merchant_wallet")
    void topUpMerchantWallet(@Param("orderId") UUID orderId);
}

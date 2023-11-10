package com.kinamulen.binarfood.repository;

import com.kinamulen.binarfood.dto.invoice.ReportRow;
import com.kinamulen.binarfood.entity.Merchant;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    Optional<Merchant> findByMerchantName(String name);

    List<Merchant> findByOpenTrue();

    @Query(value = "select\n" +
            "\tod.created_at,   \n" +
            "\tp.product_name,\n" +
            "\tp.price,\n" +
            "\tod.quantity,\n" +
            "\tsum(od.total_price)\n" +
            "from\n" +
            "\tmerchants m\n" +
            "join merchant_details md on\n" +
            "\tmd.merchant_id = m.id\n" +
            "join wallets w on\n" +
            "\tw.id = md.wallet_id\n" +
            "join products p on\n" +
            "\tp.merchant_id = m.id\n" +
            "join order_details od on\n" +
            "\tod.product_id = p.id\n" +
            "join orders o on\n" +
            "\tod.order_id = o.id\n" +
            "where\n" +
            "\tm.id = :merId\n" +
            "\tand\n" +
            "\tod.created_at < :endDate\n" +
            "\tand od.created_at >= :startDate\n" +
            "\tand\n" +
            "\to.completed = true\n" +
            "group by\n" +
            "\tod.created_at,\n" +
            "\tp.product_name,\n" +
            "\tp.price,\n" +
            "\tod.quantity;", nativeQuery = true)
    List<ReportRow> findReportByMerchantId(@Param("merId") UUID merId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}

package com.kinamulen.binarfood.repository;

import com.kinamulen.binarfood.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}

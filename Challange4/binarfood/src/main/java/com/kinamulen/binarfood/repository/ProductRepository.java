package com.kinamulen.binarfood.repository;

import com.kinamulen.binarfood.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}

package com.kinamulen.binarfood.repository;

import com.kinamulen.binarfood.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value =
            "select p.id, product_name, price , p.is_deleted, p.created_at , p.updated_at , p.deleted_at, p.merchant_id\n" +
            "from products p\n" +
            "inner join merchants m on p.merchant_id = m.id\n" +
            "where m.open = true \n" +
            "and \n" +
            "p.is_deleted = false\n" +
            "and\n" +
            "m.is_deleted = false;", nativeQuery = true)
    List<Product> findProductByOpenMerchant();
}

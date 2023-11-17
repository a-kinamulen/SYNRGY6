package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE products SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "products")
public class Product extends AuditModel{

    private String productName;
    private Double price;

//    private UUID merchantId; //foreign key to merchant
    @ManyToOne
    @JoinColumn(name="merchant_id", nullable=false)
    private Merchant merchant;

    @OneToMany(mappedBy="product", cascade = CascadeType.ALL) //mappedBy nya nembak di OrderDetail
    private Set<OrderDetail> ordersDetails;

    @Builder
    public Product(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                   String productName, Double price,
                   Merchant merchant, Set<OrderDetail> ordersDetails) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.productName = productName;
        this.price = price;
        this.merchant = merchant;
        this.ordersDetails = ordersDetails;
    }
}

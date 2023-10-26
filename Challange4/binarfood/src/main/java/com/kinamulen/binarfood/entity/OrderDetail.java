package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE order_details SET is_deleted = true, deleted_at = now() WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "order_details")
public class OrderDetail extends AuditModel{

    private Integer quantity;
    private Double totalPrice;

//    private UUID orderId; //foreign key to order
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

//    private UUID productId; //foreign key to product
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Builder
    public OrderDetail(UUID id, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                       Integer quantity, Double totalPrice,
                       Order order, Product product) {
        super(id, isDeleted, createdAt, updatedAt, deletedAt);
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.order = order;
        this.product = product;
    }
}

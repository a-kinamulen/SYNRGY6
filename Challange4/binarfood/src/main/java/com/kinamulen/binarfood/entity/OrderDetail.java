package com.kinamulen.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id //ini menjadikannya primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
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
}
